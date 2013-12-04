/**
 * Copyright (C) 2013, all rights reserved.
 * Company	SHENZHEN YUNZHONGFEI TECHNOLOGY CORP., LTD. 
 * Author	lailong
 * Since	2013-10-17
 */

/**
 * 
 * @Author lailong
 * @Since 2013-10-17
 */
public class Test {

	public int foo(int a, int b) {
		return (a + b) * (a - b);
	}

	public static void main(String[] args) {
		final Test test = new Test();
		System.out.println(test.foo(5, 3));
	}
}
