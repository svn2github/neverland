package com.peiandsky;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class Person {
	// ������е���
	int[] pokes;
	// ���ѡ���Ƶı�־
	boolean[] pokesFlag;
	// ������������ϵ�����
	int top, left;
	// ���ID
	int id;
	// ����������ӵ�ʵ��
	Desk desk;
	// ������һ����
	Card card;
	DDZ ddz;

	int paintDir = PokeType.dirV;
	Bitmap pokeImage;

	private Person last;
	private Person next;

	public Person(int[] pokes, int top, int left, int paintDir, int id,
			Desk desk, DDZ ddz) {
		this.desk = desk;
		this.id = id;
		this.pokes = pokes;
		pokesFlag = new boolean[pokes.length];
		this.setPos(left, top);
		this.paintDir = paintDir;
		this.ddz = ddz;
		pokeImage = BitmapFactory.decodeResource(ddz.getResources(),
				R.drawable.poker3552);
	}

	// ����������¼ҹ�ϵ
	public void setPosition(Person last, Person next) {
		this.last = last;
		this.next = next;
	}

	public void setPos(int l, int t) {
		this.left = l;
		this.top = t;
	}

	// ����������е���
	public void paint(Canvas canvas) {
		Rect src = new Rect();
		Rect des = new Rect();
		for (int i = 0; i < pokes.length; i++) {
			int row = Poke.getImageRow(pokes[i]);
			int col = Poke.getImageCol(pokes[i]);
			// �������NPCʱ��������ƣ��˿���ȫ�Ǳ���
			if (paintDir == PokeType.dirV) {
				row = 4;
				col = 4;
				src.set(col * 35, row * 52, col * 35 + 35, row * 52 + 52);
				des.set(left, top + i * 8, left + 35, top + 52 + i * 8);
			} else {
				// ��ǰ��һ���
				row = Poke.getImageRow(pokes[i]);
				col = Poke.getImageCol(pokes[i]);
				int select = 0;
				if (pokesFlag[i]) {
					select = 10;
				}
				src.set(col * 35, row * 52, col * 35 + 35, row * 52 + 52);
				des.set(left + i * 13, top - select, left + 35 + i * 13, top
						- select + 52);

			}
			canvas.drawBitmap(pokeImage, src, des, null);
		}

	}

	// �жϳ��Ƶ��˹�����
	public Card chupaiAI(Card card) {
		int[] pokeWanted = null;

		if (card == null) {
			// ��������һ����
			pokeWanted = Poke.outCardByItsself(pokes, last, next);
		} else {
			// �����Ҫ��һ�ֱ�card�����
			pokeWanted = Poke.findTheRightCard(card, pokes, last, next);
		}
		// ������ܳ��ƣ��򷵻�
		if (pokeWanted == null) {
			return null;
		}
		// ����Ϊ���Ƶĺ������������ƴ���������޳�
		int num = 0;
		for (int i = 0; i < pokeWanted.length; i++) {
			for (int j = 0; j < pokes.length; j++) {
				if (pokes[j] == pokeWanted[i]) {
					pokes[j] = -1;
					num++;
					break;
				}
			}
		}
		int[] newpokes = new int[0];
		if (pokes.length - pokeWanted.length > 0) {
			newpokes = new int[pokes.length - pokeWanted.length];
		}
		int j = 0;
		for (int i = 0; i < pokes.length; i++) {
			if (pokes[i] != -1) {
				newpokes[j] = pokes[i];
				j++;

			}
		}
		this.pokes = newpokes;
		Card thiscard = new Card(pokeWanted, pokeImage, id);
		// �����������һ����
		desk.currentCard = thiscard;
		this.card = thiscard;
		return thiscard;
	}

	public Card chupai(Card card) {
		int count = 0;
		for (int i = 0; i < pokes.length; i++) {
			if (pokesFlag[i]) {
				count++;
			}
		}
		int[] cardPokes = new int[count];
		int j = 0;
		for (int i = 0; i < pokes.length; i++) {
			if (pokesFlag[i]) {
				cardPokes[j] = pokes[i];
				j++;
			}
		}
		int cardType = Poke.getPokeType(cardPokes);
		System.out.println("cardType:" + cardType);
		if (cardType == PokeType.error) {
			Poke.show("������ϴ���", 100);
			// ���ƴ���
			return null;
		}
		Card thiscard = new Card(cardPokes, pokeImage, id);
		if (card == null) {
			desk.currentCard = thiscard;
			this.card = thiscard;

			int[] newPokes = new int[pokes.length - count];
			int ni = 0;
			for (int i = 0; i < pokes.length; i++) {
				if (!pokesFlag[i]) {
					newPokes[ni] = pokes[i];
					ni++;
				}
			}
			this.pokes = newPokes;
			this.pokesFlag = new boolean[pokes.length];

			return thiscard;
		} else {

			if (Poke.compare(thiscard, card)) {
				desk.currentCard = thiscard;
				this.card = thiscard;

				int[] newPokes = new int[pokes.length - count];
				int ni = 0;
				for (int i = 0; i < pokes.length; i++) {
					if (!pokesFlag[i]) {
						newPokes[ni] = pokes[i];
						ni++;
					}
				}
				this.pokes = newPokes;
				this.pokesFlag = new boolean[pokes.length];

				return thiscard;
			} else {
				Poke.show("��̫С��", 100);
				return null;
			}
		}
	}

	// ������Լ�����ʱ���������¼��Ĵ���
	public void onTuch(View v, MotionEvent event) {

		int x = (int) event.getX();
		int y = (int) event.getY();

		for (int i = pokes.length - 1; i >= 0; i--) {
			// �ж��������Ʊ�ѡ�У����ñ�־
			if (Poke.inRect(x, y, left + i * 13, top - (pokesFlag[i] ? 10 : 0),
					35, 52)) {
				pokesFlag[i] = !pokesFlag[i];
				break;
			}
		}
	}
}
