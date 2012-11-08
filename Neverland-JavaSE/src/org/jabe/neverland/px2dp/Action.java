package org.jabe.neverland.px2dp;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Action {
	public static final float SCALE = 1.5F;
	
	public static synchronized void ExeLaout(String inPath, String outPath,
			JButton jb, JFrame jf) {
		jb.setEnabled(false);
		if (!inPath.endsWith(File.separator)) {
			inPath = inPath + File.separator;
		}
		if (!outPath.endsWith(File.separator))
			outPath = outPath + File.separator;
		try {
			File f = new File(inPath);
			String[] list = f.list();
			for (String string : list) {
				if (!string.endsWith("xml")) {
					continue;
				}
				File file = new File(inPath + string);
				File oFile = new File(outPath + string);
				oFile.createNewFile();
				FileWriter fw = new FileWriter(oFile);

				BufferedReader input = new BufferedReader(new FileReader(file));
				StringBuffer buffer = new StringBuffer();
				String text;
				while ((text = input.readLine()) != null) {
					buffer.append(text + "\n");
				}
				String toString = buffer.toString();
				Pattern p = Pattern.compile("\"(\\d+)px\"");

				Matcher m = p.matcher(toString);
				while (m.find()) {
					String group = m.group();
					group = group.replaceAll("\"", "");
					group = group.replaceAll("px", "");
					int dI = px2dip(Float.parseFloat(group));
					group = "\"" + dI + "dp\"";
					toString = toString.replace(m.group(), group);
				}

				fw.write(toString);
				fw.flush();
				fw.close();
			}

			JOptionPane.showMessageDialog(jf.getContentPane(), "操作成功");
			Desktop.getDesktop().open(new File(outPath));
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(jf.getContentPane(), "操作失败");
		} finally {
			jb.setEnabled(true);
		}
	}

	public static synchronized void ExeStyle(String inPath, String outPath,
			JButton jb, JFrame jf) {
		jb.setEnabled(false);
		if (!inPath.endsWith(File.separator)) {
			inPath = inPath + File.separator;
		}
		if (!outPath.endsWith(File.separator))
			outPath = outPath + File.separator;
		try {
			File f = new File(inPath);
			String[] list = f.list();
			for (String string : list) {
				if (!string.endsWith("xml")) {
					continue;
				}
				File file = new File(inPath + string);
				File oFile = new File(outPath + string);
				oFile.createNewFile();
				FileWriter fw = new FileWriter(oFile);

				BufferedReader input = new BufferedReader(new FileReader(file));
				StringBuffer buffer = new StringBuffer();
				String text;
				while ((text = input.readLine()) != null) {
					buffer.append(text + "\n");
				}
				String toString = buffer.toString();
				Pattern p = Pattern.compile("(\\d+)px");

				Matcher m = p.matcher(toString);
				while (m.find()) {
					String group = m.group();
					group = group.replaceAll("\"", "");
					group = group.replaceAll("px", "");
					int dI = px2dip(Float.parseFloat(group));
					group = dI + "dp";
					toString = toString.replace(m.group(), group);
				}

				fw.write(toString);
				fw.flush();
				fw.close();
			}

			JOptionPane.showMessageDialog(jf.getContentPane(), "操作成功");
			Desktop.getDesktop().open(new File(outPath));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(jf.getContentPane(), "操作失败");
		} finally {
			jb.setEnabled(true);
		}
	}

	private static int px2dip(float pxValue) {
		return (int) (pxValue / SCALE + 0.5F);
	}
	
	private static int dip2px(float dpValue) {
		return (int) (dpValue * SCALE + 0.5F);
	}
}
