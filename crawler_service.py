import requests
from bs4 import BeautifulSoup
import json
from flask import Flask, jsonify

app = Flask(__name__)

@app.route('/crawl', methods=['GET'])
def crawl_data():
    # TODO: Implement your crawler logic here and fetch data
    cookies = json.load(open('cookies.json'))
    res = requests.get("https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?offset=&host_mid=37507923&timezone_offset=-60&platform=web&features=itemOpusStyle,listOnlyfans"
                       ,headers={'User-Agent':'Mozilla/5.0'},
                       cookies=cookies) 
    item1 = res.json()['data']['items']
    res = requests.get("https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?offset=&host_mid=3493265644980448&timezone_offset=-60",headers={'User-Agent':'Mozilla/5.0'},
                       cookies=cookies) 
    item2 = res.json()['data']['items']
    def process(item,fname):
        try:
            File_ts = open(fname,'r+')
            ts =json.load(File_ts)
            File_ts.close()
        except:
            ts = json.loads('{"pub_ts": 0}')
        if item[1]['type'] == "DYNAMIC_TYPE_DRAW":
            if ts['pub_ts'] != item[1]['modules']['module_author']['pub_ts']:
                if item[1]['modules']['module_dynamic']['major']['type'] == 'MAJOR_TYPE_OPUS':
                    text = item[1]['modules']['module_dynamic']['major']['opus']['summary']['text']#item[1]['modules']['module_dynamic']['desc'][1]['orig_text']
                    pictures = [url['url'] for url in item[1]['modules']['module_dynamic']['major']['opus']['pics']]
                else:
                    text = item[1]['modules']['module_dynamic']['desc']['text']
                    pictures = [url['src'] for url in item[1]['modules']['module_dynamic']['major']['draw']['items']]
                ts['pub_ts'] = item[1]['modules']['module_author']['pub_ts']
                data = {
                    "update": True,
                    "text": text,
                    "pictures": pictures
                }
                with open(fname,"w",encoding="utf-8") as File_ts:
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
                with open(fname,"w",encoding="utf-8") as File_ts:
                    File_ts.write(json.dumps(ts))
            else:
                data = {"update": False}

        else:
            data = {"update": False}
        return data
    data={}
    data['top']=[process(item1,'ts1.json'),process(item2,'ts2.json')]

    #File_ts.close()
    return jsonify(data)


if __name__ == "__main__":
    #crawl_data()
    app.run(port=8081)
