#ifndef __LOGIN_SCENE_H__
#define __LOGIN_SCENE_H__

#include "ccbase/base.h"
#include "ccwidget/widget.h"
#include "cocos-ext.h"

USING_NS_CC;

///��½����
class CLoginScene : public CScene
{
public:
	DEFINE_SCENE_CLASS(CLoginScene);

public:
	//����������ʱ����
	virtual void onCreate();
	//���������յ���Ϣʱ����
	virtual void onMessage(int nMsg, ARRAY wParam, PARAM lParam);

public:
	void onClick(CCObject *pSender);

	void login();

protected:
	CCSprite *m_pBg;

	CCEditBox *m_pNickNameEdit;

	CButton *m_pExitButton;
	CButton *m_pLoginButton;

private:
	void testUrl();
	void urlCallback(CCNode *sender ,void *data);
};

#endif //__LOGIN_SCENE_H__