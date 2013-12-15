#include "LoginScene.h"
#include "GameNetBean.h"
#include "ResourceInc.h"
#include "OppoServer.h"

//����������ʱ����
void CLoginScene::onCreate() 
{
	CCSize ccSize = CCDirector::sharedDirector()->getWinSize();
	CCNode *root = CCSSceneReader::sharedSceneReader()->createNodeWithSceneFile(s_pGameRoomScene);
	this->addChild(root);
}

void CLoginScene::onSettingsClick(CCObject *pSender)
{
	CCLOG("on Settings click");
}

//���������յ���Ϣʱ����
void CLoginScene::onMessage(int nMsg, ARRAY wParam, PARAM lParam)
{
	switch(nMsg)
	{
	//���ӳɹ�
	case MSG_UI_CONNECTED:
		this->login();
		break;
	//�����ж�
	case MSG_UI_DISCONNECTED:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"disconnected!");
		break;
	//�������
	case MSG_UI_CONNECTERROR:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"connect error!");
		break;
	//���ӳ�ʱ
	case MSG_UI_CONNECTTIMEOUT:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"connect timeout!");
		break;
	//��½�ɹ�
	case MSG_SC_INITCLIENT:
		CSceneManager::sharedSceneManager()->closePopupBox(GETBOX(CMessageBox));
		CSceneManager::sharedSceneManager()->pushScene(CCTransitionFlipX::create(0.5, GETSCENE(CMainScene)));
		break;
	}
}

void CLoginScene::onClick(CCObject *pSender)
{
	//�˳���ť
	if(pSender == m_pExitButton)
	{
		//CSceneManager::sharedSceneManager()->exit();
		this->testUrl();
	}
	//��½��ť
	else if(pSender == m_pLoginButton)
	{
		if(CGameNetBean::sharedNetBean()->isConnected())
		{
			this->login();
		}
		else
		{
			CGameNetBean::sharedNetBean()->connect();
		}
	}
}

void CLoginScene::login()
{
	const char* pNickName = m_pNickNameEdit->getText();
	if(strlen(pNickName) > 0) {
		CGameNetBean::sharedNetBean()->cs_login(pNickName);
	} else {
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"type your name!");
	}
}

void CLoginScene::testUrl()
{
	CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CLoadingBox), (void*)(""));
	OppoServer* pServer = OppoServer::shareOppoServer();
	CCHttpRequest* request = CHttpServer::getGetRequestByUrl("http://www.bsdfsaidu.com");
	request->setResponseCallback(this, callfuncND_selector(CLoginScene::urlCallback));
	pServer->send(request);
	request->release();
}

void CLoginScene::urlCallback(CCNode *sender ,void *data)
{
	CSceneManager::sharedSceneManager()->closePopupBox(GETBOX(CLoadingBox));
	CCHttpResponse* response = (CCHttpResponse*) data;
	if (!response) 
	{
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"failure");
		return;
	}
	int statusCode = response->getResponseCode();
	if (response->isSucceed()) 
	{
		
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"success");
	} else 
	{
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"failure");
	}
}