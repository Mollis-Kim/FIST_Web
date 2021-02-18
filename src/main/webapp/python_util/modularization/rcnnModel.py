import os
import sys
import json
import datetime
import skimage.draw
import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

import pickle
import joblib

import tensorflow as tf
import keras
import json

from pandas import DataFrame
from mrcnn.config import Config
from mrcnn import model as modelib, utils

import sharedVariables as sv

import time
"----------------------------------------------------------------------------------------------------------------------"
""" ##### Deep Learning ##### """


splash2 = None


class StemConfig(Config):
    NAME = 'Tree'
    IMAGES_PER_GPU = 2
    NUM_CLASSES = 1 + 1
    STEPS_PER_EPOCH = 100
    DETECTION_MIN_CONFIDENCE = 0.9

"데이터 가공 이미지 처리"
class StemDataset(utils.Dataset):
    def load_stem(self, dataset_dir, subset):
        self.add_class('tree', 1, 'tree')
        assert subset in ['train', 'val']
        dataset_dir = os.path.join(dataset_dir, subset)
        annotations = json.load(open(os.path.join(dataset_dir, 'via_region_data.json')))
        annotations = list(annotations.values())
        annotations = [a for a in annotations if a['regions']]

        for a in annotations:
            if type(a['regions']) is dict:
                polygons = [r['shape_attributes'] for r in a['regions'].values()]
            else:
                polygons = [r['shape_attri3butes'] for r in a['regions']]
            image_path = os.path.join(dataset_dir, a['filename'])
            image = skimage.io.imread(image_path)
            height, width = image.shape[:2]

            self.add_image(
                'tree',
                image_id=a['filename'],
                path=image_path,
                width=width, height=height,
                polygons=polygons)

    """가공된 데이터에서 설정한 영역 마스크 불러오기"""
    def load_mask(self, image_id):
        image_info = self.image_info[image_id]
        if image_info['source'] != 'stem':
            return super(self.__class__, self).load_mask(image_id)

        info = self.image_info[image_id]
        mask = np.zeros([info['height'], info['width'], len(info['polygons'])],
                        dtype=np.uint8)

        for i, p in enumerate(info['ploygons']):
            if p['name'] == 'polygon':
                rr, cc = skimage.draw.polygon(p['all_points_y'], p['all_points_x'])
                mask[rr, cc, i] = 1
            elif p['name'] == 'circle':
                rr, cc = skimage.draw.circle(p['cy'], p['cx'], p['r'])
                mask[rr, cc, i] = 1
        return mask.astype(np.bool), np.ones([mask.shape[-1]], dtype=np.int32)

    """결과 이미지 저장 경로"""
    def image_reference(self, image_id):
        """Return the path of the image"""
        info = self.image_info[image_id]
        if info['source'] == 'stem':
            return info['path']
        else:
            super(self.__class__, self).image_reference(image_id)


"색 변경 함수"
def color_splash(image, mask):
    global splash2
    from skimage.measure import find_contours
    gray = skimage.color.gray2rgb(skimage.color.rgb2gray(image)) * 255
    black = skimage.color.gray2rgb(skimage.color.rgb2gray(image)) * 0

    if mask.shape[-1] > 0:
        mask = (np.sum(mask, -1, keepdims=True) >= 1)
        splash = np.where(mask, image, gray).astype(np.uint8)
    else:
        splash = gray.astype(np.uint8)

    splash2 = np.where(mask, (255, 255, 255), black).astype(np.uint8)

    sv.ori_img = image[:, :, ::-1]
    sv.oriimg = image[:, :, ::-1]
    sv.splash2 = splash2[:, :, ::-1]

    return splash

"검출된 이미지 색 변경"
def detect_and_color_splash(model, image_path=None, video_path=None):
    global st
    assert image_path or video_path
    if image_path:
        image = skimage.io.imread(image_path)
        # 이미지의 width가 height보다 클때 시계방향으로 90도 회전
        h, w = image.shape[:2]
        if h < w:
            image = cv2.rotate(image, cv2.ROTATE_90_CLOCKWISE)
        r = model.detect([image], verbose=0)[0]
        sv.mask2 = r['masks']
        sv.ori_img = image[:, :, ::-1]
        splash = color_splash(image, r['masks'])   
class InferenceConfig(StemConfig):
    GPU_COUNT = 1
    IMAGES_PER_GPU = 1



def createRCNN_Model(imgPath):
    global st
    sv.config = InferenceConfig()
    #sv.config.display()
    sv.model =modelib.MaskRCNN(mode='inference', config=sv.config, model_dir='')
    sv.model.load_weights(sv.hdfPath, by_name=True)
    detect_and_color_splash(sv.model, image_path=imgPath, video_path=None)


def saveModel():
    sv.config = InferenceConfig()
    sv.config.display()
    sv.model =modelib.MaskRCNN(mode='inference', config=sv.config, model_dir='')
    sv.model.load_weights(sv.hdfPath, by_name=True)
    with open("/Users/himuc/Desktop/modularization/model/model.pck","wb") as fw:
        pickle.dump(sv.model,fw)

def loadModel():
    imgPath = "/Users/himuc/Desktop/fists_dataset/train/8.jpg"
    with open("/Users/himuc/Desktop/modularization/model/model.pck","rb") as fr:
        sv.model = pickle.load(fr)
    detect_and_color_splash(sv.model, image_path=imgPath, video_path=None)

def save_model(out_fname="model.json"):
    sv.config = InferenceConfig()
    #sv.config.display()
    sv.model =modelib.MaskRCNN(mode='inference', config=sv.config, model_dir='')
    sv.model.load_weights(sv.hdfPath, by_name=True)
    jsonObj = sv.model.to_json()
    with open(out_fname, "w") as fh:
        fh.write(jsonObj)

def load_model():
    global model
    imgPath = "/Users/himuc/Desktop/fists_dataset/train/8.jpg"
    jsonObj = None
    with open("model.json", "r") as f:
        jsonObj=f.read()
    sv.model = keras.models.model_from_json(jsonObj)
    #print(sv.model==model)
    #detect_and_color_splash(sv.model, image_path=imgPath, video_path=None)


# sv.config = InferenceConfig()
# model =modelib.MaskRCNN(mode='inference', config=sv.config, model_dir='')
# model.load_weights(sv.hdfPath, by_name=True)
# jsonObj = model.model_to_json()
# with open("mymodel.bin", "wb") as fh:
#         fh.write(model)
# with open("model.json", "r") as f:
#         jsonObj=f.read()
# sv.model = keras.models.model_from_json(jsonObj)        

#saveModel()
#loadModel()
#save_model()
#load_model()
