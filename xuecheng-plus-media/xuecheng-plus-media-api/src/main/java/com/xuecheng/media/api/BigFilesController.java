package com.xuecheng.media.api;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 上传视频
 *
 * @author xuqizheng
 * @date 2023/10/22
 */
@Api(value = "大文件上传接口", tags = "大文件上传接口")
@RestController
public class BigFilesController {
    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation(value = "文件上传前检测文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkfile(@RequestParam("fileMd5") String fileMd5) {
        return mediaFileService.checkFile(fileMd5);
    }

    @ApiOperation(value = "分块上传前检测文件")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file, @RequestParam("fileMd5") String fileMd5, @RequestParam("chunk") int chunk) throws IOException {
        //创建一个临时文件
        File tempFile=File.createTempFile("minio",".temp");
        //将获取到文件拷贝到临时文件中
        file.transferTo(tempFile);
        //文件路径
        String localFilePath = tempFile.getAbsolutePath();
        return mediaFileService.uploadChunk(fileMd5,chunk,localFilePath);
    }

    @ApiOperation(value = "合并文件")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5, @RequestParam("fileName") String fileName, @RequestParam("chunkTotal") int chunkTotal) {
        Long companyId=1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(fileName);
        uploadFileParamsDto.setTags("视频文件");
        uploadFileParamsDto.setFileType("001002");
        return mediaFileService.mergeChunks(companyId,fileMd5,chunkTotal,uploadFileParamsDto);
    }

}
