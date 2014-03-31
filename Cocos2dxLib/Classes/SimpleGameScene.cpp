#include "SimpleGameScene.h"
#include "GameNetBean.h"
#include "ccwidget/widget.h"

//����������ʱ����
void CSimpleGameScene::onCreate()
{
	pGameLayer = CGamePlayerLayer::create();
	this->addChild(pGameLayer);

	//���ذ�ť
	pBackFont = CCMenuItemFont::create("back", this, menu_selector(CSimpleGameScene::onClick));
	pBackFont->setAnchorPoint(ccp(1,0));
	pBackFont->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width, 0));
	
	CWidgetLayout *pLayout = CWidgetLayout::create();
	pLayout->addChild(pBackFont);
	this->addChild(pLayout);
}

void CSimpleGameScene::onClick(CCObject *pSender)
{
	//���ذ�ť���Ͽ����磬�ص���½
	CGameNetBean::sharedNetBean()->close();
	CSceneManager::sharedSceneManager()->popScene();
}

//���������յ���Ϣʱ����
void CSimpleGameScene::onMessage(int nMsg, ARRAY wParam, PARAM lParam)
{
	switch(nMsg)
	{
	case MSG_SC_OTHERPLAYER_RUN:
		pGameLayer->otherPlayerMove(
			wParam->GetInt(0),
			wParam->GetInt(1),
			wParam->GetInt(2));
		break;
	case MSG_SC_OTHERPLAYER_JOIN:
		pGameLayer->createOtherPlayer(wParam->GetInt(0));
		break;
	case MSG_SC_OTHERPLAYER_EXIT:
		pGameLayer->removeOtherPlayer(wParam->GetInt(0));
		break;
	//�ж�����
	case MSG_UI_DISCONNECTED:
		CSceneManager::sharedSceneManager()->popScene();
		break;
	}
}