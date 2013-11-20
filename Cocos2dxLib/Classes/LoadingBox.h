#ifndef __LOADING_BOX_H__
#define __LOADING_BOX_H__

#include "ccbase/base.h"
#include "ccwidget/widget.h"

class CLoadingBox : public CPopupBox
{
public:
	DEFINE_POPUPBOX_CLASS(CLoadingBox);

public:
	///����������ʱ����
	virtual void onCreate();
	///���������򿪵�ʱ�����
	virtual void onOpen(PARAM param);

	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);

private:
	CLabel *m_pMessageLabel;

};

#endif //__LOADING_BOX_H__