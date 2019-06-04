package com.waibao.project.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.waibao.project.bean.Backup;
import com.waibao.project.mapper.BackupMapper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class TestQuartz  extends QuartzJobBean {

	@Autowired
	BackupMapper backupMapper;
	/**
	 * 执行定时任务
	 */
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("执行定时任务》》》"+new Date());
		String filePath="C:\\Users\\13108\\Desktop\\预约系统备份\\";
		String fileName="预约系统备份文件";
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String time=df.format(new Date());
		String dbName="project";//备份的数据库名
		String username="root";//用户名
		String password="root";//密码
		File uploadDir = new File(filePath);
		if (!uploadDir.exists())
			uploadDir.mkdirs();

        String cmd =  "cmd /c c:\\mysqldump  -u"+ username +"  -p"+password +" "+ dbName + " -r "
                + filePath + "/" + fileName+time+ ".sql";
        try {
        Process process = Runtime.getRuntime().exec(cmd);
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Integer i = backupMapper.BackupAdd(df1.format(new Date()),"系统自动备份");
        System.out.println("备份数据库成功!!!");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
     
        }

	}

}
