#ifndef __LOGIN_SCENE_H__
#define __LOGIN_SCENE_H__

#include "ccbase/base.h"
#include "ccwidget/widget.h"

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
	CCEditBox *m_pNickNameEdit;

	CButton *m_pExitButton;
	CButton *m_pLoginButton;

};

#endif //__LOGIN_SCENE_H__