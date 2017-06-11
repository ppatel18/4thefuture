#!python

from flask import Flask
from flask import make_response
from flask import jsonify
from flask import request
# from flask import Response
# from flask_pymongo import PyMongo
from bson import dumps
from logging.handlers import RotatingFileHandler
import requests
import random
import traceback
# import flask_pymongo
from time import strftime
# import json
from aimodel import content

app = Flask(__name__)
conf = {"host": '0.0.0.0',
        "port": '8081',
        "log_name": "aiserver.log"}

loghandler = RotatingFileHandler(conf['log_name'],
                                 maxBytes=10000,
                                 backupCount=3)
loghandler.setLevel(20)
app.logger.addHandler(loghandler)

#
# @app.route("/recommendation", methods=['GET'])
# def setappconfig():
#     global mongo
#     global conf
#
#     filename = BCM_CONFIG_FILE
#
#     try:
#         with open(filename) as json_file:
#             conf = json.loads(json_file.read())
#     except IOError as e:
#         e.strerror = 'Unable to load configuration file (%s)' % e.strerror
#         raise
#
#     # app.config.from_json(BCM_CONFIG_FILE)
#
#     # rollover log files at 20 MB each file and max 5 files.
#     loghandler = RotatingFileHandler(conf['log_name'],
#                                      maxBytes=conf['log_size'],
#                                      backupCount=conf['log_rotation_count'])
#     loghandler.setLevel(conf['log_level'])
#     app.logger.addHandler(loghandler)
#
#     # Mongo server configuration
#     app.config['DEBUG'] = conf['debug']
#     app.config['MONGO_HOST'] = conf['mongo_host']
#     app.config['MONGO_PORT'] = conf['mongo_port']
#     app.config['MONGO_DBNAME'] = conf['mongo_dbname']
#
#     # app.config['MONGO_AUTO_START_REQUEST'],
#     # app.config['MONGO_REPLICA_SET'], app.config['MONGO_READ_PREFERENCE'],
#
#     # app.config['MONGO_USERNAME'] = "apiserver"
#     # app.config['MONGO_PASSWORD'] = "ap1$erver"
#     # app.config['MONGO_URI']
#
#     mongo = PyMongo(app)
#
# setappconfig()
#
#
# @app.route("/s3dataevent", methods=['PUT'])
# def s3dataevent():
#     try:
#         s3event_collection = mongo.db[conf['s3event_collection_name']]
#         s3event_collection.insert_one(request.json)
#         return Response(status=201)
#     except Exception as e:
#         e.status_code = 500
#         raise e
#

@app.route("/detectobjects", methods=['POST'])
def detectObjects():
    '''build bounding boxes and confidence scores of objects of interest
    in an image.'''
    object_detection_endpoint = conf['image_api_host'] + \
                                "/object_detection"
    object_detection_params = {
        'img_name': request.json['image'],
        'interest_objs': dumps(request.json['interest_objs']),
        'blur_objs': dumps(request.json['blur_objs'])}

    try:
        resp = requests.get(object_detection_endpoint,
                            params=object_detection_params)
        if resp.status_code == 200:
            return make_response(jsonify(resp.json()), 200)
        else:
            return make_response(jsonify({'error': "Please check post data"}), resp.status_code)
    except Exception as err:
        err.status_code = 503
        exceptions(err)
        return make_response(jsonify({'error': "Exception connecting image server"}), 503)


@app.route("/detectzones", methods=['POST'])
def detectZones():
    '''construct a summary of the location of people in an image
        Args:
            zones: list of bounding boxes where each bounding box of a zone is in shape [[x_min, y_min], [x_max, y_max]]
    '''
    zone_detection_endpoint = conf['image_api_host'] + '/zone_detection'
    zone_detection_params = {
        'zones': dumps(request.json['zones']),
        'img_name': request.json['image'],
        'interest_objs': dumps(request.json['interest_objs']),
        'blur_objs': dumps(request.json['blur_objs'])}
    try:
        resp = requests.get(zone_detection_endpoint, params=zone_detection_params)
        if resp.status_code == 200:
            return make_response(jsonify(resp.json()), 200)
        else:
            return make_response(jsonify({'error': "Please check post data"}), resp.status_code)
    except Exception as err:
        err.status_code = 503
        exceptions(err)
        return make_response(jsonify({'error': "Exception connecting image server"}), 503)


# @app.after_request
# def after_request(response):
#     timestamp = strftime('[%Y-%b-%d %H:%M]')
#     app.logger.info('%s %s %s %s %s %s',
#                     timestamp, request.remote_addr, request.method,
#                     request.scheme, request.full_path, response.status)
#     return response

@app.route("/courses/<string:searchStr>/", methods=['GET'])
def courses(searchStr):
    try:
        coursera_endpoint = "https://api.coursera.org/api/courses.v1"
        qp = {
            'q': "search",
            'query': searchStr,
            'primaryLanguages': "en",
            'fields': ['photoUrl', 'domainTypes', 'categories']
            }
        resp = requests.get(coursera_endpoint, params=qp)
        if resp.status_code == 200:
            return make_response(jsonify(resp.json()), 200)
        else:
            return make_response(jsonify({'error': "Please check post data"}), resp.status_code)

    except Exception as err:
        err.status_code = 503
        exceptions(err)
        return make_response(jsonify({'error': "Exception connecting courses server"}), 503)


@app.route("/recommendations/<string:subject>/<int:progress>/<int:age>/", methods=['GET'])
# @app.route("/config/<string:uuid>/<int:ver>/", methods=['GET'])
def recommendation(subject, progress, age):
    try:
        # config_collection = mongo.db[conf['config_collection_name']]
        # config_cursor = config_collection.find(filter={conf['config_id_name']: uuid}).sort(
        #     conf['config_ver_name'], flask_pymongo.DESCENDING)
        # c = None
        # if config_cursor.count() > 0:
        #     c = config_cursor.next()
        #
        # else:
        #     app.logger.info("No configs available for " +
        #                     conf['config_id_name'] + ": " + str(uuid))
        #
        # config_cursor.close()
        # if (c):
        #     resp = make_response(dumps(c), 200)
        #     resp.headers['Content-Type'] = 'application/json'
        #     return resp
        # else:
        #     resp = make_response(
        #         jsonify({'error': "No configs exist for given resource"}), 404)
        #     return resp
        if subject in content.keys():
            recs = content[subject]
            random.shuffle(recs)
            resp = make_response(jsonify({"recommendations": recs[:5]}), 200)
            resp.headers['Content-Type'] = 'application/json'
            return resp

    except Exception as err:
        err.status_code = 404
        raise err



@app.errorhandler(Exception)
def exceptions(e):
    tb = traceback.format_exc()
    timestamp = strftime('[%Y-%b-%d %H:%M]')
    app.logger.error('%s %s %s %s %s 5xx INTERNAL SERVER ERROR\n%s',
                     timestamp, request.remote_addr, request.method,
                     request.scheme, request.full_path, tb)
    return e.status_code


if __name__ == "__main__":
    app.run(host=conf["host"], port=conf["port"])
