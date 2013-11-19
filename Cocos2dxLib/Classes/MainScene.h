#ifndef __MAIN_SCENE_H__
#define __MAIN_SCENE_H__

#include "ccbase/base.h"
#include "GamePlayerLayer.h"

class CMainScene : public CScene
{
public:
	DEFINE_SCENE_CLASS(CMainScene);

public:
	//����������ʱ����
	virtual void onCreate();
	//���������յ���Ϣʱ����
	virtual void onMessage(int nMsg, ARRAY wParam, PARAM lParam);

	void onClick(CCObject *pSender);

private:
	CGamePlayerLayer *pGameLayer;

	CCMenuItemFont *pBackFont;
};

#endif //__MAIN_SCENE_H__