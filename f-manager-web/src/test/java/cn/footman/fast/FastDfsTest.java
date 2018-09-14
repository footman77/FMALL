package cn.footman.fast;

import cn.footman.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDfsTest {

    @Test
    public void testUpload() throws Exception{
        //创建一个配置文件，内容是tracker服务器的地址

        //使用全局对象加载配置文件
        ClientGlobal.init("D:\\javaweb\\FMall\\f-manager-web\\src\\main\\resources\\conf\\client.conf");
        //创建一个TrackerClientD对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackClient获得一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StorageServer的引用，可以是null
        StorageServer storageServer = null;
        //创建一个StorageClient,参数需要TrackerServer StorageServer
        StorageClient storageClient = new StorageClient(trackerServer,storageServer);
        //使用StorageClient上传文件
        String[] strings = storageClient.upload_file("E:\\picture\\nice\\genji.jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }

    }

    @Test
    public void testFastDFSClient() throws Exception{

        FastDFSClient fastDFSClient = new FastDFSClient("D:\\javaweb\\FMall\\f-manager-web\\src\\main\\resources\\conf\\client.conf");
        String s = fastDFSClient.uploadFile("E:\\picture\\nice\\23-130402112Z5.jpg");
        System.out.println(s);
    }
}
