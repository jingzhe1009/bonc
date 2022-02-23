package com.ljz.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
	
	public static void read(String filePath) {
		FileInputStream fis=null;
        BufferedInputStream bis=null;
		try {
            fis=new FileInputStream(filePath);
            bis=new BufferedInputStream(fis);
            String content=null;
             //自定义缓冲区
            byte[] buffer=new byte[10240];
            int flag=0;
            while((flag=bis.read(buffer))!=-1){
                content+=new String(buffer, 0, flag);
            }
            System.out.println(content);
            //关闭的时候只需要关闭最外层的流就行了
           
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public static void write(String filePath,String content,String encode) throws Exception{
		FileOutputStream fos =null;
		BufferedOutputStream bos= null;
		try {
            fos=new FileOutputStream(filePath);
            bos=new BufferedOutputStream(fos);
            bos.write(content.getBytes());
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	try {
				bos.flush();
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public static String formatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		return sdf.format(date);
	}

}
