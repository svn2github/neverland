#ifndef __OPPO_SERVER__
#define __OPPO_SERVER__

#include "cocos2d.h"
#include "ccnet/httpserver.h"

class OppoServer
{
private:
	OppoServer();
	~OppoServer();
	
public:

	//��������
	virtual void send(CCHttpRequest* pRequest);
	//�����½ TODO
	virtual void requestLogin();

private:
	//����Server
	CHttpServer* m_pServerDelegate;

public:
	//����ģʽ
	static OppoServer* shareOppoServer();
};




#endif