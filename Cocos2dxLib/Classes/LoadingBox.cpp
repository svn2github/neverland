#include "LoadingBox.h"
#include "ResourceInc.h"

///����������ʱ����
void CLoadingBox::onCreate()
{
	CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCSprite *pBg = CCSprite::create(s_pLoadingIcon);

	this->setCached(false);
	this->setTouchPriority(POPUPBOX_PRIORITY);	//���Ƚ��յ��¼�
	this->setContentSize(pBg->getContentSize());
	this->setPosition(CSceneManager::getCenterPoint());

	//pBg->setAnchorPoint(CCPointZero);
	//pBg->setPosition(CCPointZero);
	this->addChild(pBg);

	CCRepeatForever *repeat = CCRepeatForever::create( CCRotateBy::create(1.0f, 360) );
	pBg->runAction(repeat);

	m_pMessageLabel = NULL;
}

///�������򱻴򿪵�ʱ�����
void CLoadingBox::onOpen(PARAM param)
{
	if(param == NULL)
		return;

	const char *pStr = (char*) param;
	if (m_pMessageLabel != NULL)
	{
		m_pMessageLabel->setString(pStr);
	}
}

bool CLoadingBox::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	this->closeSelf();
	return true;
}