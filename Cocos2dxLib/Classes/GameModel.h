#ifndef __GAMEMODEL_H__
#define __GAMEMODEL_H__

#include <map>
using namespace std;

class CPlayer;
class COtherPlayer;
class CGameModel;

//������ҵ����ݽṹ
class COtherPlayer
{
public:
	int m_nOtherPlayerID;	//�������ID
	string m_strName;		//����ǳ�
	int m_nX;				//����
	int m_nY;
};

//������ҵ����ݽṹ
class CPlayer
{
public:
	int m_nPlayerID;		//���ID
	string m_strName;		//����ǳ�
	int m_nX;				//����
	int m_nY;
};

//��Ϸ��������
class CGameModel
{
public:
	//�����
	CPlayer m_Player;
	//���������������б�
	map<int, COtherPlayer> m_mOtherPlayerMap;

public:
	//����
	static CGameModel* sharedGameModel()
	{
		static CGameModel* model = NULL;
		if(model == NULL)
		{
			model = new CGameModel();
		}
		return model;
	};
};




#endif //__GAMEMODEL_H__