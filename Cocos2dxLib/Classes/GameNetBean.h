#ifndef __GAMENETBEAN_H__
#define __GAMENETBEAN_H__

#include "ccnet/net.h"
#include "ccbase/base.h"
#include "GameModel.h"

#define MSG_UI_CONNECTED 1					//֪ͨUI���ӳɹ�
#define MSG_UI_DISCONNECTED 2				//֪ͨUI�����ж�
#define MSG_UI_CONNECTERROR	3				//֪ͨUI���Ӵ���
#define MSG_UI_CONNECTTIMEOUT 4				//֪ͨUI���ӳ�ʱ

///cs�ӿ���Ϣ��
#define MSG_CS_LOGIN 1000					//��½
#define MSG_CS_RUN	1001					//�ܲ�
#define MSG_CS_EXIT	1002					//�뿪����

///sc�ӿ���Ϣ��
#define MSG_SC_ERROR 5000					//������Ϣ
#define MSG_SC_INITCLIENT 5001				//��ʼ���ͻ���
#define MSG_SC_OTHERPLAYER_RUN 5002			//��������ܲ�
#define MSG_SC_OTHERPLAYER_JOIN	5003		//������Ҽ��뷿��
#define MSG_SC_OTHERPLAYER_EXIT 5004		//��������뿪����

///�����



class CGameNetBean : public CNetBean, public CLooper
{
public:
	DEFINE_SINGLE_FUNCTION(CGameNetBean);

public:
	virtual ~CGameNetBean();

public:
	///�������ɹ� ��ʼ����ʱ����
	virtual void onCreate();
	///�����ӳɹ�ʱ����
	virtual void onConnected();
	///���Ͽ�����ʱ����
	virtual void onDisconnected();
	///�����Ӵ���ʱ����
	virtual void onConnectError();
	///�����ӳ�ʱʱ����
	virtual void onConnectTimeout();
	///��������Ϣʱ����
	virtual void onMessage(STREAM stream);

public:
	//ÿ֡�ص�����
	virtual void loop() { this->drive(); }



//sc����˷����ͻ��˵Ļص��ӿ�
public:
	//��Ϸ�����еĴ�����Ϣ���ؽӿ�
	//������[int:��Ϣ��] [int:�����]
	void sc_error(STREAM stream);

	//��½�ɹ�����ã�֪ͨ�ͻ��˳�ʼ������
	//������[int:��Ϣ��] [int:��ҵ�id��] [str:��ҵ��ǳ�] [int:��ҵĳ�ʼx����] [int:��ҵĳ�ʼy����]
	//[int:$count�������Ѵ��ڵ��������]
	//$count * {[int:�������id] [str:�����������] [int:�������x����] [int:�������y����]}
	void sc_initclient(STREAM stream);

	//��������ܲ���
	//������[int:��Ϣ��] [int:�������id] [int:x����] [int:y����]
	void sc_otherplayer_run(STREAM stream);

	//������ҽ��뷿����
	//������[int:��Ϣ��] [int:�������id] [str:�����������] [int:������ҵ�x����] [int:������ҵ�y����]
	void sc_otherplayer_join(STREAM stream);

	//��������뿪����
	//������[int:��Ϣ��] [int:�������id]
	void sc_otherplayer_exit(STREAM stream);


//cs�ͻ��˷�������˵Ľӿ�
public:
	//�����½�����û���û�����Ĭ����ע�ᣬ��½֮��ͽ��뷿����
	//������[int:��Ϣ��] [str:�Զ��������]
	//���أ�[ok:sc_initclient] [no:sc_error]
	void cs_login(const char* nikename);

	//����ܲ���֪ͨ�������������
	//������[int:��Ϣ��] [int:���id] [int:x����] [int:y����]
	void cs_run(int x, int y);

	//�����뿪���䣬֪ͨ�������������
	//������[int:��Ϣ��] [int:���id]
	void cs_exit();

};





#endif //__GAMENETBEAN_H__