package com.wyhw.plugins.plugin;

//import com.wyhw.plugins.plugin.encrypt.UnclosedInputStream;
//import com.wyhw.plugins.plugin.encrypt.UnclosedOutputStream;
//import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
//import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
//import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
//
//import java.io.*;
//import java.util.jar.Manifest;
//import java.util.logging.Logger;

/**
 * @author wanyanhw
 * @since 2021/12/20 13:52
 */
public class JarEncryptUtil {

	static {
		System.loadLibrary("encrypt");
	}

//	private static Logger logger = Logger.getGlobal();

	public native static byte[] encrypt(byte[] bytes);

//	public static void encrypt(String src, String desc) throws Exception {
//		File srcFile = new File(src);
//		File descFile = new File(desc);
//		try (FileInputStream fis = new FileInputStream(srcFile);
//		     FileOutputStream fos = new FileOutputStream(descFile);
//		     JarArchiveInputStream jis = new JarArchiveInputStream(fis);
//		     JarArchiveOutputStream jos = new JarArchiveOutputStream(fos);) {
//
//			UnclosedInputStream nis = new UnclosedInputStream(jis);
//			UnclosedOutputStream nos = new UnclosedOutputStream(jos);
//
//			JarArchiveEntry jarArchiveEntry;
//			while ((jarArchiveEntry = jis.getNextJarEntry()) != null) {
//				long time = jarArchiveEntry.getTime();
//				String name = jarArchiveEntry.getName();
//				JarArchiveEntry descJarEntry = new JarArchiveEntry(name);
//				descJarEntry.setTime(time);
//				if (jarArchiveEntry.isDirectory()) {
//					jos.putArchiveEntry(descJarEntry);
//				} else if (name.equals("META-INF/MANIFEST.MF")) {
//					Manifest manifest = new Manifest(nis);
//					jos.putArchiveEntry(descJarEntry);
//					manifest.write(nos);
//				} else if (name.startsWith("BOOT-INF/classes/")) {
//					jos.putArchiveEntry(descJarEntry);
//					if (name.contains("com/zk/") && name.endsWith(".class")) {
//						transfer(encryptCus(nis), nos);
//						logger.info(name + " 文件已加密");
//					} else {
//						transfer(nis, nos);
//					}
//				} else if (name.startsWith("BOOT-INF/lib/")) {
//					descJarEntry.setMethod(JarArchiveEntry.STORED);
//					descJarEntry.setSize(jarArchiveEntry.getSize());
//					descJarEntry.setCrc(jarArchiveEntry.getCrc());
//					jos.putArchiveEntry(descJarEntry);
//					transfer(nis, nos);
//				} else {
//					jos.putArchiveEntry(descJarEntry);
//					transfer(nis, nos);
//				}
//				jos.closeArchiveEntry();
//			}
//			jos.finish();
//		}
//	}
//
//	private static InputStream encryptCus(InputStream nis) throws IOException {
//		try (BufferedInputStream bis = new BufferedInputStream(nis);
//		     ByteArrayOutputStream bos = new ByteArrayOutputStream();) {
//			byte[] buffer = new byte[4096];
//			int len;
//			while ((len = bis.read(buffer)) != -1) {
//				bos.write(buffer, 0, len);
//			}
//			bos.flush();
//			byte[] bytes = bos.toByteArray();
//			encrypt(bytes);
//			return new ByteArrayInputStream(bytes);
//		}
//	}
//
//	/**
//	 * 输入流传输到输出流
//	 *
//	 * @param in  输入流
//	 * @param out 输出流
//	 * @throws IOException I/O 异常
//	 */
//	private static void transfer(InputStream in, OutputStream out) throws IOException {
//		byte[] buffer = new byte[4096];
//		int length;
//		while ((length = in.read(buffer)) != -1) {
//			out.write(buffer, 0, length);
//		}
//		out.flush();
//	}
}
