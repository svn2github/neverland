#include "MessageBox.h"

///����������ʱ����
void CMessageBox::onCreate()
{
	CCSize winSize = CCDirector::sharedDirector()->getWinSize();
	CCSprite *pBg = CCSprite::create("message_box_bg.png");

	this->setCached(false);
	this->setTouchPriority(POPUPBOX_PRIORITY);	//���Ƚ��յ��¼�
	this->setContentSize(pBg->getContentSize());
	this->setPosition(ccp( (winSize.width - pBg->getContentSize().width) / 2,
		(winSize.height - pBg->getContentSize().height) / 2));

	pBg->setAnchorPoint(CCPointZero);
	pBg->setPosition(CCPointZero);
	this->addChild(pBg);

	m_pMessageLabel = CLabel::create();
	m_pMessageLabel->setFontSize(30);
	m_pMessageLabel->setPosition(ccp(this->getContentSize().width / 2, this->getContentSize().height / 2));
	this->addChild(m_pMessageLabel);

}

///�������򱻴򿪵�ʱ�����
void CMessageBox::onOpen(PARAM param)
{
	if(param == NULL)
		return;

	const char *pStr = (char*) param;
	m_pMessageLabel->setString(pStr);
}

bool CMessageBox::ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent)
{
	//�������˵�������������򣬾͹رյ�����
	CCPoint ccPoint = pTouch->getLocation();
	if(!this->boundingBox().containsPoint(ccPoint))
	{
		this->closeSelf();
		return true;
	}
	return true;
}