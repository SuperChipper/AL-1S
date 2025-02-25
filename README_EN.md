# AL-1S

[English](./README_EN.md) | [中文](./README.md)

<div align="center">
  <img src="./al1s/表情7.png"/>
</div>

## Overview

AL-1S is a project based on Mirai, developed using [itbaima-robot-starter](https://github.com/itbaima-study/itbaima-robot-starter), and runs as a QQ bot on a Spring Boot service.

It integrates real-time conversation features using Tsinghua University's open-source [ChatGLM3](https://github.com/SuperChipper/ChatGLM3). Alternatively, users can choose to utilize OpenAI's service (requires a self-provided API key), implemented in [openai_api](src/main/java/com/example/demo/Listener/ChatGPT.java).

Additionally, it supports real-time interaction with QQ images through Zhipu API, implemented in [ZhipuApi](src/main/java/com/example/demo/Listener/ZhipuApi.java).

The interaction mode can be selected at runtime.

[Mirai Project Repository](https://github.com/mamoe/mirai)

## User Guide

### System Requirements

To fully experience the ChatGLM conversation feature, a GPU with at least 16GB of VRAM is required for smooth operation. (Alternatively, you can modify the request format and endpoint in `ChatGPT.java` to directly use ChatGPT.)

### Installation

Add the following configuration to `application.yaml` under the `src\main\resources` directory:

```yaml
server:
  port: 8080
itbaima:
  robot:
     username:
     login-type: password
     password:
     protocol: android_pad
     signer:
       version: 8.9.58               # Client version used
       url: http://127.0.0.1:8888    # Deployed signature server address
       type: kiliokuara              # Signature server type
       authorization-key: kfc        # Authentication key, default: kfc
       server-identity-key: vivo50   # Server identity key, default: vivo50
     data:
       work-dir: robot-data
       cache-dir: cache
       contact-cache: false
       save-device-id: true
```

You need to provide the QQ account credentials for deployment. Additionally, QQ Android tablet login requires an authentication server.

For detailed deployment instructions:
[Installation Guide](https://github.com/itbaima-study/itbaima-robot-starter/wiki)

### Using Zhipu API for Conversations

To enable this feature, create an `api.json` file in the root directory of the project with the following format:

```json
{"api_key": "your_zhipu_api_key"}
```

## Changelog

- **08/04/2024**: Added web scraping for Blue Archive CN server Bilibili updates.
- **16/04/2024**: Implemented ChatGLM4V for generating replies to multimedia messages.

## License

This project is open-source under the GPL 3.0 license.

