#include "cocos2d.h"
#include "CCEGLView.h"
#include "AppDelegate.h"

#include "GameNetBean.h"

#include "ccbase/base.h"
#include "LoginScene.h"
#include "MainScene.h"
#include "MessageBox.h"
#include "LoadingBox.h"

USING_NS_CC;

AppDelegate::AppDelegate()
{
}

AppDelegate::~AppDelegate()
{
}

bool AppDelegate::applicationDidFinishLaunching()
{
    CCDirector *pDirector = CCDirector::sharedDirector();
    pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());
	CCEGLView::sharedOpenGLView()->setDesignResolutionSize(1280, 720, kResolutionShowAll);
    pDirector->setDisplayStats(true);
    pDirector->setAnimationInterval(1.0 / 60);

	//设置网络对象
	CGameNetBean::sharedNetBean()->setAddress("192.168.4.145", 7789);

	//注册所有场景
	REGISTE_SCENE_CLASS(CLoginScene);
	REGISTE_SCENE_CLASS(CMainScene);

	//注册弹出框
	REGISTE_POPUPBOX_CLASS(CMessageBox);
	REGISTE_POPUPBOX_CLASS(CLoadingBox);

	//运行场景
	CSceneManager::sharedSceneManager()->runScene(GETSCENE(CLoginScene));

    return true;
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground()
{
    CCDirector::sharedDirector()->stopAnimation();
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground()
{
    CCDirector::sharedDirector()->startAnimation();
}
