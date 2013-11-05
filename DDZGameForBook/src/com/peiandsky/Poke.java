package com.peiandsky;

import java.util.Random;
import java.util.Vector;

import android.widget.Toast;

public class Poke {
	public static Random rand = new Random();

	public static void show(String text, int time) {
		Toast t = Toast.makeText(DDZ.ddz, text, time);
		t.show();
	}

	public static boolean inRect(int x, int y, int rectX, int rectY, int rectW,
			int rectH) {
		if (x < rectX || x > rectX + rectW || y < rectY || y > rectY + rectH) {
			return false;
		}
		return true;
	}

	// 0-53��ʾ54����
	public static void shuffle(int[] pokes) {
		int len = pokes.length;
		// ����54�����е��κ�һ�ţ��������һ�ź�������������˳����ҡ�
		for (int l = 0; l < len; l++) {
			int des = rand.nextInt(54);
			int temp = pokes[l];
			pokes[l] = pokes[des];
			pokes[des] = temp;
		}
	}

	public static int getDZ() {
		return rand.nextInt(3);
		// return 0;
	}

	// ��pokes���дӴ�С���򣬲���ð������
	public static void sort(int[] pokes) {
		for (int i = 0; i < pokes.length; i++) {
			for (int j = i + 1; j < pokes.length; j++) {
				if (pokes[i] < pokes[j]) {
					int temp = pokes[i];
					pokes[i] = pokes[j];
					pokes[j] = temp;
				}
			}
		}
	}

	/**
	 * 16С����17����
	 */
	public static int getPokeValue(int poke) {
		// ���˿�ֵΪ52ʱ����С��
		if (poke == 52) {
			return 16;
		}
		// ���˿�ֵΪ53ʱ���Ǵ���
		if (poke == 53) {
			return 17;
		}
		// ��������·�����Ӧ��ֵ(3,4,5,6,7,8,9,10,11(J),12(Q),13(K),14(A),15(2))
		return poke / 4 + 3;
	}

	public static int getImageRow(int poke) {
		return poke / 13;
	}

	public static int getImageCol(int poke) {
		return poke % 13;
	}

	/**
	 * �ǲ���һ����Ч������
	 * 
	 * @param pokes
	 * @return
	 */
	public static boolean isCard(int[] pokes) {
		if (getPokeType(pokes) == PokeType.error)
			return false;
		return true;
	}

	/**
	 * pokes�е��Ƶ�˳��Ҫ�����Ƶ�ֵ����,˳���в�����2
	 * 
	 * @param pokes
	 * @return
	 */
	public static int getPokeType(int[] pokes) {
		int len = pokes.length;
		// ��������Ϊ1ʱ,����
		if (len == 1) {
			return PokeType.danpai;
		}
		// ��������Ϊ2ʱ,�����Ƕ��ƺͻ��
		if (len == 2) {
			if (pokes[0] == 53 && pokes[1] == 52) {
				return PokeType.huojian;
			}
			if (getPokeValue(pokes[0]) == getPokeValue(pokes[1])) {
				return PokeType.duipai;
			}
		}
		// ������Ϊ3ʱ,ֻ��������˳
		if (len == 3) {
			if (getPokeValue(pokes[0]) == getPokeValue(pokes[1])
					&& getPokeValue(pokes[2]) == getPokeValue(pokes[1])) {
				return PokeType.sanzhang;
			}
		}
		// ������Ϊ4ʱ,����������һ��ը��
		if (len == 4) {
			int firstCount = getPokeCount(pokes, pokes[0]);
			if (firstCount == 3 || getPokeCount(pokes, pokes[1]) == 3) {
				return PokeType.sandaiyi;
			}
			if (firstCount == 4) {
				return PokeType.zhadan;
			}
		}
		// ����������5ʱ,�ж��ǲ��ǵ�˳
		if (len >= 5) {
			if (shunzi(pokes)) {
				return PokeType.danshun;
			}

		}
		// ������Ϊ6ʱ,�Ĵ���
		if (len == 6) {
			boolean have4 = false;
			boolean have1 = false;
			for (int i = 0; i < len; i++) {
				int c = getPokeCount(pokes, pokes[i]);
				if (c == 4) {
					have4 = true;
				}
				if (c == 1) {
					have1 = true;
				}
			}

			if (have4 && have1) {
				return PokeType.sidaier;
			}
		}
		// ���������ڵ���6ʱ,�ȼ���ǲ���˫˳����˳
		if (len >= 6) {
			// ˫˳
			boolean shuangshunflag = true;
			for (int i = 0; i < len; i++) {
				if (getPokeCount(pokes, pokes[i]) != 2) {
					shuangshunflag = false;
					break;
				}
			}
			if (shuangshunflag) {
				int[] tempPokes = new int[len / 2];
				for (int i = 0; i < len / 2; i++) {
					tempPokes[i] = pokes[i * 2];
				}
				if (shunzi(tempPokes)) {
					return PokeType.shuangshun;
				}
			}
			System.out.println("shuangshun:" + shuangshunflag);
			// ��˳
			boolean sanshunflag = true;
			for (int i = 0; i < len; i++) {
				if (getPokeCount(pokes, pokes[i]) != 3) {
					sanshunflag = false;
					break;
				}
			}
			if (sanshunflag) {
				int[] tempPokes = new int[len / 3];
				for (int i = 0; i < len / 3; i++) {
					tempPokes[i] = pokes[i * 3];
				}
				if (shunzi(tempPokes)) {
					return PokeType.sanshun;
				}
			}

		}

		// ���������ڵ���8,���ܹ���4����ʱ,�ж��ǲ��Ƿɻ�
		if (len >= 8 && len % 4 == 0) {
			UniqInt ui = new UniqInt();
			int have1 = 0;
			for (int i = 0; i < pokes.length; i++) {
				int c = getPokeCount(pokes, pokes[i]);
				if (c == 3) {
					ui.addInt(pokes[i]);
				} else if (c == 1) {
					have1++;
				}
			}
			if (ui.size() == have1) {
				int[] tempArray = ui.getArray();
				sort(tempArray);
				if (shunzi(tempArray)) {
					return PokeType.feiji;
				}
			}

		}
		// ������ǿ�֪����,���ش�����
		return PokeType.error;

	}

	/**
	 * �ж��ǲ���˳��
	 * 
	 * @param pokes
	 * @return
	 */
	public static boolean shunzi(int[] pokes) {
		int start = getPokeValue(pokes[0]);
		// ˳���в��ܰ���2,king
		if (start >= 15) {
			return false;
		}
		int next;
		for (int i = 1; i < pokes.length; i++) {
			next = getPokeValue(pokes[i]);
			if (start - next != 1) {
				return false;
			}
			start = next;
		}
		return true;
	}

	// ͳ��һ������ֵͬ���Ƴ��ֵĴ������ж��Ƕ���,��˳,����һ,ը��,�Ĵ�����
	public static int getPokeCount(int[] pokes, int poke) {
		int count = 0;
		for (int i = 0; i < pokes.length; i++) {
			if (getPokeValue(pokes[i]) == getPokeValue(poke)) {
				count++;
			}
		}
		return count;
	}

	// ͨ����������һ����,������������ֵ��С��pokes�е�˳�������кõ�
	public static int getPokeTypeValue(int[] pokes, int pokeType) {
		// �⼸������ֱ�ӷ��ص�һ��ֵ
		if (pokeType == PokeType.danpai || pokeType == PokeType.duipai
				|| pokeType == PokeType.danshun || pokeType == PokeType.sanshun
				|| pokeType == PokeType.shuangshun
				|| pokeType == PokeType.sanzhang || pokeType == PokeType.zhadan) {
			return getPokeValue(pokes[0]);
		}
		// ����һ�ͷɻ���������Ϊ3���Ƶ������ֵ
		if (pokeType == PokeType.sandaiyi || pokeType == PokeType.feiji) {
			for (int i = 0; i <= pokes.length - 3; i++) {
				if (getPokeValue(pokes[i]) == getPokeValue(pokes[i + 1])
						&& getPokeValue(pokes[i + 1]) == getPokeValue(pokes[i + 2])) {
					return getPokeValue(pokes[i]);
				}
			}
		}
		// �Ĵ�����������Ϊ4����ֵ
		if (pokeType == PokeType.sidaier) {
			for (int i = 0; i < pokes.length - 3; i++) {
				if (getPokeValue(pokes[i]) == getPokeValue(pokes[i + 1])
						&& getPokeValue(pokes[i + 1]) == getPokeValue(pokes[i + 2])
						&& getPokeValue(pokes[i + 2]) == getPokeValue(pokes[i + 3])) {
					return getPokeValue(pokes[i]);
				}
			}
		}
		return 0;
	}

	/**
	 * true ��һ����
	 * 
	 * @param f
	 * @param s
	 * @return
	 */
	public static boolean compare(Card f, Card s) {
		// ������������ͬʱ
		if (f.pokeType == s.pokeType) {
			// ������������ͬʱ��������ͬ���޷��Ƚϣ�Ĭ��Ϊ�ڶ�����ʹs���ܳ���
			if (f.pokes.length != s.pokes.length)
				return false;
			// ������ͬ��������ͬʱ���Ƚ���ֵ
			return f.value > s.value;
		}
		// �����Ͳ�ͬ��ʱ��,���f�������ǻ��,�򷵻�true
		if (f.pokeType == PokeType.huojian) {
			return true;
		}
		if (s.pokeType == PokeType.huojian) {
			return false;
		}
		// �ų���������ͣ�ը�����
		if (f.pokeType == PokeType.zhadan) {
			return true;
		}
		if (s.pokeType == PokeType.zhadan) {
			return false;
		}
		// �޷��Ƚϵ������Ĭ��Ϊs����f
		return false;
	}

	public int[] findBigThanCard(Card card, int pokes[]) {
		return null;

	}

	public static int[] outCardByItsself(int pokes[], Person last, Person next) {
		AnalyzePoke analyze = AnalyzePoke.getInstance();
		analyze.setPokes(pokes);
		int cardArray[] = null;
		Vector<int[]> card_danpai = analyze.getCard_danpai();
		Vector<int[]> card_sanshun = analyze.getCard_sanshun();

		int danpai = card_danpai.size();
		int sanshun = card_sanshun.size();

		int[] miniType = analyze.getMinType(last, next);
		System.out.println("miniType:" + miniType[0] + "," + miniType[1]);
		switch (miniType[0]) {
		case PokeType.sanshun:
			// �ȳ���˳�ͷɻ�
			System.out.println("sanshun is over");
			if (sanshun > 0) {
				cardArray = card_sanshun.elementAt(miniType[1]);

				if (cardArray.length / 3 < danpai) {
					int[] desArray = new int[cardArray.length / 3 * 4];
					for (int i = 0; i < cardArray.length; i++) {
						desArray[i] = cardArray[i];
					}
					for (int j = 0; j < cardArray.length / 3; j++) {
						desArray[cardArray.length + j] = card_danpai
								.elementAt(j)[0];
					}
					Poke.sort(desArray);
					return desArray;
				} else {
					return cardArray;
				}
			}
			break;
		case PokeType.shuangshun:
			System.out.println("shuangshun is over");
			Vector<int[]> card_shuangshun = analyze.getCard_shuangshun();
			System.out.println("shuangshun:" + card_shuangshun.size());
			if (card_shuangshun.size() > 0) {
				cardArray = card_shuangshun.elementAt(miniType[1]);
				return cardArray;
			}
			break;
		case PokeType.danshun:
			System.out.println("danshun is over");
			Vector<int[]> card_danshun = analyze.getCard_danshun();
			if (card_danshun.size() > 0) {
				return card_danshun.elementAt(miniType[1]);
			}
			break;
		case PokeType.sanzhang:
			System.out.println("sanzhang is over");
			Vector<int[]> card_sanzhang = analyze.getCard_sanzhang();
			if (card_sanzhang.size() > 0) {
				int[] sanzhangArray = card_sanzhang.elementAt(miniType[1]);
				if (danpai > 0) {
					int newA[] = new int[] { sanzhangArray[0],
							sanzhangArray[1], sanzhangArray[2],
							card_danpai.elementAt(0)[0] };
					Poke.sort(newA);
					return newA;
				} else {
					return sanzhangArray;
				}
			}
			break;
		case PokeType.duipai:
			System.out.println("duipai is over");
			Vector<int[]> card_duipai = analyze.getCard_duipai();
			if (card_duipai.size() > 0) {
				return card_duipai.elementAt(miniType[1]);
			}
			break;
		case PokeType.danpai:
			System.out.println("danpai is over");
			if (danpai > 0) {
				return card_danpai.elementAt(miniType[1]);
			}
			break;
		}

		Vector<int[]> card_zhadan = analyze.getCard_zhadan();
		if (card_zhadan.size() > 0) {
			return card_zhadan.elementAt(0);
		}
		// ����Ҫ�ж��¼ҵ��ƣ��Ƿ���ͬ��
		// �����ĵ��ƣ��ϱ���˵ȥ�ɣ�
		return new int[] { pokes[0] };
	}

	// ��������
	public static int[] findTheRightCard(Card card, int pokes[], Person last,
			Person next) {
		AnalyzePoke an = AnalyzePoke.getInstance();
		an.setPokes(pokes);
		int c = an.remainCount();
		// �����ֻʣ��һ���Ƶ�ʱ��������ζ�Ҫ����
		if (c == 1) {
			return findBigThanCardSimple2(card, pokes, 100);
		}

		// �ж��Ҹò���Ҫ��
		if (Desk.boss != last.id && Desk.boss != next.id) {
			// ����boss����ҪҪ��
			// �ж�����ʣ������
			int pokeLength = Desk.persons[card.personID].pokes.length;
			int must = pokeLength * 100 / 17;
			if (pokeLength <= 2) {
				must = 100;
			}
			return findBigThanCardSimple2(card, pokes, must);

		} else {
			if (Desk.boss == card.personID) {
				// �ǵ��������ƣ�Ҫ��
				int pokeLength = Desk.persons[card.personID].pokes.length;
				int must = pokeLength * 100 / 17;
				if (pokeLength <= 2) {
					must = 100;
				}
				return findBigThanCardSimple2(card, pokes, must);
			} else {
				// �Ҳ��ǵ�������Ҳ���ǵ������ƣ����Լ��ҵ���
				if (card.personID == next.id) {
					// ��Ҫ�ƣ�������������������һ�γ���
					if (c <= 3) {
						return findBigThanCardSimple2(card, pokes, 100);
					}
					return null;
				} else {
					// �ƵĴ�С�������һ��ֵ�Ҳ�Ҫ��������˳һ��
					if (card.value < 12) {
						int pokeLength = Desk.persons[card.personID].pokes.length;
						int must = 100 - pokeLength * 100 / 17;
						if (pokeLength <= 4) {
							must = 0;
						}
						AnalyzePoke ana = AnalyzePoke.getInstance();
						ana.setPokes(next.pokes);
						if (ana.remainCount() <= 1) {
							if (ana.lastCardTypeEq(card.pokeType)
									&& (Desk.boss == next.id || (Desk.boss != next.id && Desk.boss != last.id))) {
								return findBigThanCardSimple2(card, pokes, 100);
							}
						} else {
							return findBigThanCardSimple2(card, pokes, must);
						}

					} else {
						return null;
					}
				}
			}
		}
		return null;
	}

	// ��pokes�������ҵ���card���һ����
	public static int[] findBigThanCardSimple2(Card card, int pokes[], int must) {
		try {
			// ��ȡcard����Ϣ����ֵ������
			int[] cardPokes = card.pokes;
			int cardValue = card.value;
			int cardType = card.pokeType;
			int cardLength = cardPokes.length;
			// ʹ��AnalyzePoke�����ƽ��з���
			AnalyzePoke analyz = AnalyzePoke.getInstance();
			analyz.setPokes(pokes);

			Vector<int[]> temp;
			int size = 0;
			// �����ʵ�����ѡȡ�ʵ���
			switch (cardType) {
			case PokeType.danpai:
				temp = analyz.getCard_danpai();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					int v = Poke.getPokeValue(cardArray[0]);
					if (v > cardValue) {
						return cardArray;
					}
				}
				// ���������û�У���ѡ�����������г������4��2������һ��
				int st = 0;
				if (analyz.getCountWang() == 2) {
					st += 2;
				}
				if (analyz.getCount2() == 4) {
					st += 4;
				}
				if (Poke.getPokeValue(pokes[st]) > cardValue)
					return new int[] { pokes[st] };

				// ���ը�������ݽ����Լ��ʳ���,����¼��Ǻ��Լ�һ�����˳�Ӹ��¼�

				break;
			case PokeType.duipai:
				temp = analyz.getCard_duipai();
				size = temp.size();

				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					int v = Poke.getPokeValue(cardArray[0]);
					if (v > cardValue) {
						return cardArray;
					}
				}

				// ���������û�У�����Ҫ���˫˳
				temp = analyz.getCard_shuangshun();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					for (int j = cardArray.length - 1; j > 0; j--) {
						int v = Poke.getPokeValue(cardArray[j]);
						if (v > cardValue) {
							return new int[] { cardArray[j], cardArray[j - 1] };
						}
					}
				}
				// ���˫˳��û�У�����Ҫ�������
				temp = analyz.getCard_sanzhang();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					int v = Poke.getPokeValue(cardArray[0]);
					if (v > cardValue) {
						return new int[] { cardArray[0], cardArray[1] };
					}
				}
				// ���������û�У���Ϳ���ը�����¼�Ҳ����˳��

				break;
			case PokeType.sanzhang:
				temp = analyz.getCard_sanzhang();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					int v = Poke.getPokeValue(cardArray[0]);
					if (v > cardValue) {
						return cardArray;
					}
				}
				break;
			case PokeType.sandaiyi:
				if (pokes.length < 4) {
					break;
				}
				boolean find = false;
				int[] sandaiyi = new int[4];
				temp = analyz.getCard_sanzhang();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] cardArray = temp.get(i);
					int v = Poke.getPokeValue(cardArray[0]);
					if (v > cardValue) {
						for (int j = 0; j < cardArray.length; j++) {
							sandaiyi[j] = cardArray[j];
							find = true;
						}
					}
				}
				// û��������������
				if (!find) {
					break;
				}
				// ����һ����ϳ�����һ
				temp = analyz.getCard_danpai();
				size = temp.size();
				if (size > 0) {
					int[] t = temp.get(0);
					sandaiyi[3] = t[0];
				} else {
					temp = analyz.getCard_danshun();
					size = temp.size();
					for (int i = 0; i < size; i++) {
						int[] danshun = temp.get(i);
						if (danshun.length >= 6) {
							sandaiyi[3] = danshun[0];
						}
					}
				}
				// ���������һ����С��
				if (sandaiyi[3] == 0) {
					for (int i = pokes.length - 1; i >= 0; i--) {
						if (Poke.getPokeValue(pokes[i]) != Poke
								.getPokeValue(sandaiyi[0])) {
							sandaiyi[3] = pokes[i];
						}
					}
				}
				if (sandaiyi[3] != 0) {
					Poke.sort(sandaiyi);
					return sandaiyi;
				}
				break;
			case PokeType.danshun:// ��ֵ���Ż�
				temp = analyz.getCard_danshun();
				size = temp.size();
				for (int i = 0; i < size; i++) {
					int[] danshun = temp.get(i);
					if (danshun.length == cardLength) {
						if (cardValue < Poke.getPokeValue(danshun[0])) {
							return danshun;
						}
					}
				}
				for (int i = 0; i < size; i++) {
					int[] danshun = temp.get(i);
					if (danshun.length > cardLength) {
						if (danshun.length < cardLength
								|| danshun.length - cardLength >= 3) {
							if (rand.nextInt(100) < must) {
								if (cardValue >= Poke.getPokeValue(danshun[0])) {
									continue;
								}

								int index = 0;
								for (int k = 0; k < danshun.length; k++) {
									if (cardValue < Poke
											.getPokeValue(danshun[k])) {
										index = k;
									} else {
										break;
									}
								}

								if (index + cardLength > danshun.length) {
									index = danshun.length - cardLength;
								}
								int[] newArray = new int[cardLength];
								int n = 0;
								for (int m = index; m < danshun.length; m++) {
									newArray[n++] = danshun[m];
								}
								return newArray;
							}
							break;
						}
						if (cardValue >= Poke.getPokeValue(danshun[0])) {
							continue;
						}
						int start = 0;
						int end = 0;
						if (danshun.length - cardLength == 1) {
							if (cardValue < Poke.getPokeValue(danshun[1])) {
								start = 1;
							} else {
								start = 0;
							}
						} else if (danshun.length - cardLength == 2) {
							if (cardValue < Poke.getPokeValue(danshun[2])) {
								start = 2;
							} else if (cardValue < Poke
									.getPokeValue(danshun[1])) {
								start = 1;
							} else {
								start = 0;
							}
						}
						int[] dan = new int[cardLength];
						int m = 0;
						for (int k = start; k < danshun.length; k++) {
							dan[m++] = danshun[k];
						}
						return dan;
					}
				}
				break;
			case PokeType.shuangshun:
				temp = analyz.getCard_shuangshun();
				size = temp.size();

				for (int i = size - 1; i >= 0; i--) {
					int cardArray[] = temp.get(i);
					if (cardArray.length < cardLength) {
						continue;
					}

					if (cardValue < Poke.getPokeValue(cardArray[0])) {
						if (cardArray.length == cardLength) {
							return cardArray;
						} else {
							int d = (cardArray.length - cardLength) / 2;
							int index = 0;
							for (int j = cardArray.length - 1; j >= 0; j--) {
								if (cardValue < Poke.getPokeValue(cardArray[j])) {
									index = j / 2;
									break;
								}
							}

							int total = cardArray.length / 2;
							int cardTotal = cardLength / 2;
							if (index + cardTotal > total) {
								index = total - cardTotal;
							}
							int shuangshun[] = new int[cardLength];
							int m = 0;
							for (int k = index * 2; k < cardArray.length; k++) {
								shuangshun[m++] = cardArray[k];
							}
							return shuangshun;
						}
					}
				}
				break;
			case PokeType.sanshun:
				temp = analyz.getCard_sanshun();
				size = temp.size();
				for (int i = size - 1; i >= 0; i--) {
					int[] cardArray = temp.get(i);
					if (cardLength > cardArray.length) {
						continue;
					}

					if (cardValue < Poke.getPokeValue(cardArray[0])) {
						if (cardLength == cardArray.length) {
							return cardArray;
						} else {
							int[] newArray = new int[cardLength];
							for (int k = 0; k < cardLength; k++) {
								newArray[k] = cardArray[k];
							}
							return newArray;
						}
					}
				}
				break;
			case PokeType.feiji:
				// ��ʱ������
				break;
			case PokeType.zhadan:
				temp = analyz.getCard_zhadan();
				size = temp.size();
				int zd[] = null;
				if (size > 0) {
					for (int i = 0; i < size; i++) {
						zd = temp.elementAt(i);
						if (cardValue < Poke.getPokeValue(zd[0])) {
							return zd;
						}
					}
				}
				break;
			case PokeType.huojian:
				return null;
			case PokeType.sidaier:
				// ��ʱ������,�����������
				break;
			}
			// TODO �������һ���Գ��꣬������ζ�Ҫ�������������
			// ����must��ֵ���ж�Ҫ�Ƶı�Ҫ��
			boolean needZd = false;
			if (must < 90) {
				must *= 0.2;
				if (rand.nextInt(100) < must) {
					needZd = true;
				}
			} else {
				needZd = true;
			}
			if (needZd) {
				temp = analyz.getCard_zhadan();
				size = temp.size();
				if (size > 0) {
					return temp.elementAt(size - 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// public static int[] findBigThanCardSimple(Card card, int pokes[]) {
	// int cardType = card.pokeType;
	// int value = card.value;
	// int[] pokeWanted = new int[card.pokes.length];
	// boolean find = false;
	//
	// if (card.pokes.length <= pokes.length) {
	// switch (cardType) {
	// case PokeType.danpai:
	// for (int i = pokes.length - 1; i >= 0; i--) {
	// if (getPokeValue(pokes[i]) > value) {
	// pokeWanted[0] = pokes[i];
	// // pokes[i] = -1;
	// find = true;
	// break;
	// }
	// }
	// break;
	// case PokeType.duipai:
	// for (int i = pokes.length - 1; i > 0; i--) {
	// if (getPokeValue(pokes[i]) > value
	// && getPokeValue(pokes[i]) == getPokeValue(pokes[i - 1])) {
	// pokeWanted[0] = pokes[i];
	// pokeWanted[1] = pokes[i - 1];
	// // pokes[i] = -1;
	// // pokes[i - 1] = -1;
	// find = true;
	// break;
	// }
	// }
	// break;
	// case PokeType.sanzhang:
	// for (int i = pokes.length - 1; i > 1; i--) {
	// if (getPokeValue(pokes[i]) > value
	// && getPokeValue(pokes[i]) == getPokeValue(pokes[i - 1])
	// && getPokeValue(pokes[i]) == getPokeValue(pokes[i - 2])) {
	// pokeWanted[0] = pokes[i];
	// pokeWanted[1] = pokes[i - 1];
	// pokeWanted[2] = pokes[i - 2];
	// // pokes[i] = -1;
	// // pokes[i - 2] = -1;
	// // pokes[i - 1] = -1;
	// find = true;
	// break;
	// }
	// }
	// break;
	// case PokeType.sandaiyi:
	// for (int i = pokes.length - 1; i >= 2; i--) {
	// if (getPokeValue(pokes[i]) > value
	// && getPokeValue(pokes[i]) == getPokeValue(pokes[i - 1])
	// && getPokeValue(pokes[i]) == getPokeValue(pokes[i - 2])) {
	// pokeWanted[0] = pokes[i];
	// pokeWanted[1] = pokes[i - 1];
	// pokeWanted[2] = pokes[i - 2];
	// // pokes[i] = -1;
	// // pokes[i - 2] = -1;
	// // pokes[i - 1] = -1;
	//
	// if (i + 1 >= pokes.length) {
	// if (i - 3 > 0) {
	// find = false;
	// } else {
	// pokeWanted[3] = pokes[i - 3];
	// // pokes[i - 3] = -1;
	// find = true;
	// break;
	// }
	// } else {
	// pokeWanted[3] = pokes[i + 1];
	// // pokes[i + 1] = -1;
	// find = true;
	// break;
	// }
	// }
	// }
	//
	// break;
	// case PokeType.danshun:// test
	// int minValue = value - card.pokes.length + 1;
	// for (int i = pokes.length - 1; i >= 0; i--) {
	// if (getPokeValue(pokes[i]) > minValue) {
	// int currentValue = minValue;
	// int startIdx = pokeWanted.length - 1;
	// for (int j = i; j >= 0; j--) {
	// if (getPokeValue(pokes[j]) == currentValue) {
	//
	// pokeWanted[startIdx] = pokes[j];
	// if (startIdx == 0) {
	// find = true;
	// // for (int k = j; k < j +
	// // pokeWanted.length; i++) {
	// // pokes[k] = -1;
	// // }
	// break;
	// }
	// startIdx--;
	// currentValue++;
	// } else {
	// i = j - 1;
	// break;
	// }
	// }
	// if (find) {
	// break;
	// }
	// }
	// }
	// break;
	// case PokeType.shuangshun:// test
	// int len = pokeWanted.length;
	// for (int i = pokes.length - 1; i >= len - 1; i--) {
	// pokeWanted = getPartOfArray(pokes, i - len - 1, len);
	// if (getPokeType(pokeWanted) == PokeType.shuangshun) {
	// if (getPokeValue(pokeWanted[0]) > value) {
	// find = true;
	// // for (int k = i - len - 1; k < i - 1; i++) {
	// // pokes[k] = -1;
	// // }
	// break;
	// }
	// }
	// }
	// break;
	// case PokeType.sanshun:
	// len = pokeWanted.length;
	// for (int i = pokes.length - 1; i >= len - 1; i--) {
	// pokeWanted = getPartOfArray(pokes, i - len - 1, len);
	// if (getPokeType(pokeWanted) == PokeType.sanshun) {
	// if (getPokeValue(pokeWanted[0]) > value) {
	// find = true;
	// // for (int k = i - len - 1; k < i - 1; i++) {
	// // pokes[k] = -1;
	// // }
	// break;
	// }
	// }
	// }
	// break;
	// case PokeType.feiji:
	//
	// break;
	// case PokeType.sidaier:
	// break;
	//
	// }
	// } else {
	// switch (cardType) {
	// case PokeType.zhadan:
	//
	// break;
	// case PokeType.huojian:
	// break;
	// }
	//
	// }
	// if (find) {
	// return pokeWanted;
	// } else {
	// return null;
	// }
	//
	// }

	// public static int[] getPartOfArray(int pokes[], int start, int len)//
	// ;test
	// {
	// if (len <= 0) {
	// return null;
	// }
	// int[] newArray = new int[len];
	// int length = pokes.length;
	// if (start + len >= length) {
	// return null;
	// }
	// int j = 0;
	// for (int i = start; i < start + len; i++) {
	// newArray[j++] = pokes[i];
	// }
	// return newArray;
	// }

}
