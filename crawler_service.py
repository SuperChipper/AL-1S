import requests
from bs4 import BeautifulSoup
import json
from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/crawl', methods=['GET'])
def crawl_data():
    # TODO: Implement your crawler logic here and fetch data
    try:
        File_ts = open("ts.json",'r+')
        ts =json.load(File_ts)
        File_ts.close()
    except:
        ts = json.loads('{"pub_ts": 0}')
    res = requests.get("https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?offset=&host_mid=37507923&timezone_offset=-60&platform=web&features=itemOpusStyle,listOnlyfans",headers={'User-Agent':'Mozilla/5.0'})
    item = res.json()['data']['items']
    if item[1]['type'] == "DYNAMIC_TYPE_DRAW":
        if ts['pub_ts'] != item[1]['modules']['module_author']['pub_ts']:
            text = item[1]['modules']['module_dynamic']['major']['opus']['summary']['text']
            pictures = [url['url'] for url in item[1]['modules']['module_dynamic']['major']['opus']['pics']]
            
            ts['pub_ts'] = item[1]['modules']['module_author']['pub_ts']
            data = {
                "update": True,
                "text": text,
                "pictures": pictures
            }
            with open("ts.json","w",encoding="utf-8") as File_ts:
                File_ts.write(json.dumps(ts))
        else:
            data = {"update": False}


    elif item[1]['type'] == "DYNAMIC_TYPE_AV":
        if ts['pub_ts'] != item[1]['modules']['module_author']['pub_ts']:
            text = item[1]['modules']['module_dynamic']['major']['archive']['title'] + "\n" + item[1]['modules']['module_dynamic']['major']['archive']['jump_url']
            pictures = [item[1]['modules']['module_dynamic']['major']['archive']['cover']]
            ts['pub_ts'] = item[1]['modules']['module_author']['pub_ts']
            data = {
                "update": True,
                "text": text,
                "pictures": pictures
            }
            with open("ts.json","w",encoding="utf-8") as File_ts:
                File_ts.write(json.dumps(ts))
        else:
            data = {"update": False}

    else:
        data = {"update": False}

    #File_ts.close()
    return jsonify(data)

if __name__ == "__main__":
    
    app.run(port=5000)
