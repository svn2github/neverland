#ifndef __MESSAGE_BOX_H__
#define __MESSAGE_BOX_H__

#include "ccbase/base.h"
#include "ccwidget/widget.h"

class CMessageBox : public CPopupBox
{
public:
	DEFINE_POPUPBOX_CLASS(CMessageBox);

public:
	///����������ʱ����
	virtual void onCreate();
	///���������򿪵�ʱ�����
	virtual void onOpen(PARAM param);

	virtual bool ccTouchBegan(CCTouch *pTouch, CCEvent *pEvent);

private:
	CLabel *m_pMessageLabel;

};

#endif //__MESSAGE_BOX_H__