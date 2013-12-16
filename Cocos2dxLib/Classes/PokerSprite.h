#include <cocos2d.h>
#include "ccbase/base.h"

#define IMAGE_POKER_BG_ALL_1 "gui_ui/pokers/pokerBg.png"
#define IMAGE_POKER_BG_DEFAULT "gui_ui/pokers/pokerBgDefault.png"
#define IMAGE_POKER_COLOR "gui_ui/pokers/pokerColor.png"
#define IMAGE_POKER_NUMBER "gui_ui/pokers/pokerNumber.png"
#define IMAGE_POKER_JOKER  "gui_ui/pokers/pokerJoker.png"
#define IMAGE_POKER_SELECTING "gui_ui/pokers/pokerSelected.png"
#define DDZCARDNUM 20

class PokerSprite ;
class cardShowList
{
public:
	cardShowList(){
		//CCLOG("%d",sizeof(PokerSprite*));
		cardUIList=(PokerSprite**)malloc(sizeof(PokerSprite*)*DDZCARDNUM);
		cardGap=30;
		CCLOG("%d\n",cardGap);};
	~cardShowList(){CC_SAFE_FREE(cardUIList);};
	PokerSprite **cardUIList;//=(PokerSprite*)malloc(sizeof(PokerSprite*)*DDZCARDNUM);
	int length;
	int cardGap;
};

class PokerSprite :public CCSprite, public CCTargetedTouchDelegate
{
private:
	void initFrame();
public:
	PokerSprite(){};
	//PokerSprite(){m_kind=1;m_selectStatus=0;};
	PokerSprite(char color,char number,char kind=1,char selectStatus=0)
	{
		m_kind=kind;m_selectStatus=selectStatus;m_color=color;m_number=number;
		initFrame();
	};
	~PokerSprite(){};
	static PokerSprite* create(char color,char number, cardShowList *manageOthers=0,int touchP=-100,char kind=1,char selectStatus=0)
	{
		PokerSprite *returnData=new PokerSprite();
		if (!returnData->initWithFile(IMAGE_POKER_BG_DEFAULT))
		{
			//CC_SAFE_DELETE(returnData);
			return 0;
		}
		returnData->m_kind=kind;returnData->m_selectStatus=selectStatus;
		returnData->m_color=color;returnData->m_number=number;
		returnData->m_touchPriority=touchP;
		returnData->m_selecting=0;
		returnData->m_manageothers=manageOthers;

		returnData->initFrame();

		return returnData;
	};
	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchMoved(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchEnded(CCTouch *pTouch, CCEvent *pEvent);
	virtual void ccTouchCancelled(CCTouch *pTouch, CCEvent *pEvent) {}
	virtual void registerWithTouchDispatcher(){CCDirector::sharedDirector()->getTouchDispatcher()->addTargetedDelegate(this, this->getTouchPriority(), true);}

	void onEnter();
	void onExit();

	int getTouchPriority(){return m_touchPriority;};
	void setTouchPriority(int pt){m_touchPriority=pt;};

	void endSelection();
	void doingSelection();
	void PokerSprite::doingNoSelection();
	cardShowList* m_manageothers;
private:
	//1 ��ͨ 2�� 3��
	//0 normal 1ѡ���� 2��ѡ
	char m_kind;
	char m_selectStatus;
	char m_color;//
	char m_number;

	//�����ƶ�����

	int m_touchPriority;
	CCSprite *m_selecting;
	void PokerSprite::doingSelectionOthers(CCTouch *pTouch);




	//��ɫ1-4 �ں�÷�� ����1-15 3-K��A,2,С,��
	//����colorΪ0 ���color��numberͬʱΪ0 �������




};

