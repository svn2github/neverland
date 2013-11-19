#ifndef __LOGIN_SCENE_H__
#define __LOGIN_SCENE_H__

#include "ccbase/base.h"
#include "ccwidget/widget.h"

///登陆界面
class CLoginScene : public CScene
{
public:
	DEFINE_SCENE_CLASS(CLoginScene);

public:
	//当场景创建时调用
	virtual void onCreate();
	//当场景接收到消息时调用
	virtual void onMessage(int nMsg, ARRAY wParam, PARAM lParam);

public:
	void onClick(CCObject *pSender);

	void login();

protected:
	CCEditBox *m_pNickNameEdit;

	CButton *m_pExitButton;
	CButton *m_pLoginButton;

};

#endif //__LOGIN_SCENE_H__