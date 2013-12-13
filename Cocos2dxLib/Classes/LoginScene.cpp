#include "LoginScene.h"
#include "GameNetBean.h"
#include "ResourceInc.h"
#include "OppoServer.h"

//当场景创建时调用
void CLoginScene::onCreate() 
{
	CCSize ccSize = CCDirector::sharedDirector()->getWinSize();
	CCNode *root = CCSSceneReader::sharedSceneReader()->createNodeWithSceneFile(s_pGameRoomScene);

	CCComRender *render = dynamic_cast<CCComRender*>(root->getChildByTag(10004)->getComponent("GUIComponent"));
	UILayer *layer = dynamic_cast<UILayer*>(render->getNode());
	UIPanel *panel = dynamic_cast<UIPanel*>(layer->getWidgetByName("pannel_toolbar"));
	UIButton *settings = dynamic_cast<UIButton*>(layer->getWidgetByName("button_settings"));
	settings->addReleaseEvent(this, coco_releaseselector(CLoginScene::onSettingsClick));

	CCComRender *table = dynamic_cast<CCComRender*>(root->getChildByTag(10009)->getComponent("CCSprite"));

	//table->setVisible(false);
	if (table)
	{
		CCSprite *sprite = dynamic_cast<CCSprite*>(table->getNode());
		CCLOG("get the table");
	}

	this->addChild(root);
}

void CLoginScene::onSettingsClick(CCObject *pSender)
{
	CCLOG("on Settings click");
}

//当场景接收到消息时调用
void CLoginScene::onMessage(int nMsg, ARRAY wParam, PARAM lParam)
{
	switch(nMsg)
	{
	//连接成功
	case MSG_UI_CONNECTED:
		this->login();
		break;
	//连接中断
	case MSG_UI_DISCONNECTED:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"disconnected!");
		break;
	//连错错误
	case MSG_UI_CONNECTERROR:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"connect error!");
		break;
	//连接超时
	case MSG_UI_CONNECTTIMEOUT:
		CSceneManager::sharedSceneManager()->openPopupBox(GETBOX(CMessageBox), (void*)"connect timeout!");
		break;
	//登陆成功
	case MSG_SC_INITCLIENT:
		CSceneManager::sharedSceneManager()->closePopupBox(GETBOX(CMessageBox));
		CSceneManager::sharedSceneManager()->pushScene(CCTransitionFlipX::create(0.5, GETSCENE(CMainScene)));
		break;
	}
}

void CLoginScene::onClick(CCObject *pSender)
{
	//退出按钮
	if(pSender == m_pExitButton)
	{
		//CSceneManager::sharedSceneManager()->exit();
		this->testUrl();
	}
	//登陆按钮
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