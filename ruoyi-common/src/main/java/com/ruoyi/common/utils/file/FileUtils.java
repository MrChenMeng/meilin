package com.ruoyi.common.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;

/**
 * 文件处理工具类
 * 
 * @author ruoyi
 */
public class FileUtils
{
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     * 
     * @param filePath 文件路径
     * @param os 输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0)
            {
                os.write(b, 0, length);
            }
        }
        catch (IOException e)
        {
            throw e;
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     * 
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath)
    {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists())
        {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     * 
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename)
    {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     * 
     * @param request 请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    public static String getFileExtension(String filePath) {
        return filePath.substring(filePath.lastIndexOf(".") + 1);
    }

    public static String getFileNameWithoutExtension(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf("."));
    }

    /**
     * 判断文件夹是否存在，不存在则新建
     * @param path
     */
    public static void dirExists(String path) {
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    public static boolean fileExists(String strFile) {
        try {
            File f = new File(strFile);
            return f.exists();

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查文件类型
     * @param fileName
     * @return
     */
    public static boolean checkFile(String fileName, String type) {
        boolean flag = false;
        String suffixname = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (suffixname != null && suffixname.equalsIgnoreCase(type)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 创建文件路径的父文件夹，不管文件层级，均可创建
     *
     * @param path
     *            文件路径
     * @return 是否创建成功，如果path为空或者path为null ,则返回false
     */
    public static boolean createParentDir(String path) {
        return createParentDir(path, false);
    }

    public static boolean createParentDir(File file) {
        return createParentDir(file, false);
    }

    /**
     * 创建文件路径的父文件夹，不管文件层级，均可创建
     *
     * @param path
     *            文件路径
     * @param override
     *            是否覆盖
     * @return 是否创建成功，如果path为空或者path为null ,则返回false
     */
    public static boolean createParentDir(String path, boolean override) {
        if (path == null) {
            return false;
        }
        return createDir(new File(path).getParent(), override);
    }

    public static boolean createParentDir(File file, boolean override) {
        if (file == null) {
            return false;
        }
        return createDir(file.getParent(), override);
    }

    /**
     * 创建文件夹，不管文件层级，均可创建
     *
     * @param path
     *            文件路径
     * @param override
     *            是否覆盖
     * @return 是否创建成功，如果path为空或者path为null ,则返回false
     */
    public static boolean createDir(String path, boolean override) {
        if (path == null) {
            return false;
        }
        File file = new File(path);
        if (file.exists() && !override) {
            return false;
        }
        return file.mkdirs();
    }


}
