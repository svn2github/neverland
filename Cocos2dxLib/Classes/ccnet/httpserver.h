#ifndef __HTTP_CLIENT_H__
#define __HTTP_CLIENT_H__

#include <stdio.h>
#include <stdlib.h>
#include "cocos2d.h"
#include "cocos-ext.h"

USING_NS_CC;
USING_NS_CC_EXT;

class CHttpServer
{
private:
	//���캯��
	CHttpServer(void);
	//��������
	virtual ~CHttpServer(void);
public:
	//��������
	virtual void send(CCHttpRequest* request);
public:
	//����ģʽ
	static CHttpServer* sharedHttpServer(void);
	//ͨ��url��ȡGet����
	static CCHttpRequest* getGetRequestByUrl(const char* pUrl);
	//ͨ��url��data��ȡPost����
	static CCHttpRequest* getPostRequestByUrlAndData(const char* pUrl, const char* pData);
private:
	//����Ĭ�ϵ�����ͷ
	static void addDefaultHeaders(CCHttpRequest* pRequest);
};

#endif