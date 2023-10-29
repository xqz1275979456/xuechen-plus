package com.xuecheng.media.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description 媒资文件管理业务类
 * @author Mr.M
 * @date 2022/9/10 8:55
 * @version 1.0
 */
public interface MediaFileService {

 /**
  * @description 媒资文件查询方法
  * @param pageParams 分页参数
  * @param queryMediaParamsDto 查询条件
  * @return com.xuecheng.base.model.PageResult<com.xuecheng.media.model.po.MediaFiles>
  * @author Mr.M
  * @date 2022/9/10 8:57
  */
 public PageResult<MediaFiles> queryMediaFiels(Long companyId,PageParams pageParams, QueryMediaParamsDto queryMediaParamsDto);

 /**
  * 上传文件
  * @param companyId 机构id
  * @param uploadFileParamsDto 文件信息
  * @param localFilePath 文件本地路径
  * @return UploadFileResultDto
  */
 public UploadFileResultDto uploadFile(Long companyId, UploadFileParamsDto uploadFileParamsDto, String localFilePath);

 public MediaFiles addMediaFilesToDb(Long companyId,String fileMd5,UploadFileParamsDto uploadFileParamsDto,String bucket,String objectName);

 /**
  * 检测分块文件是否存在
  *
  * @param fileMd5    文件的md5
  * @param chunkIndex 分块序号
  * @return {@link RestResponse}<{@link Boolean}> false 不存在，true存在
  */
 public RestResponse<Boolean> checkChunk(String fileMd5,int chunkIndex);

 /**
  * 检查文件是否存在
  *
  * @param fileMd5 文件的md5
  * @return {@link RestResponse}<{@link Boolean}>  false 不存在，true存在
  */
 public RestResponse<Boolean> checkFile(String fileMd5);

 /**
  * 上传分块文件
  *
  * @param fileMd5            文件的md5
  * @param chunk              f分块序号
  * @param localChunkFilePath 分块块文件本地路径
  * @return {@link RestResponse}
  */
 public RestResponse uploadChunk(String fileMd5,int chunk,String localChunkFilePath);

 /**
  * 合并分块文件
  *
  * @param companyId           机构id
  * @param fileMd5             文件的md5
  * @param chunkTotal          分块总和
  * @param uploadFileParamsDto 文件信息
  * @return {@link RestResponse}
  */
 public RestResponse mergeChunks(Long companyId,String fileMd5,int chunkTotal,UploadFileParamsDto uploadFileParamsDto);
}
