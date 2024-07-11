package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.service.IReservationsService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 预约表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/reservations")
@Slf4j
public class ReservationsController {

    @Autowired
    private IReservationsService reservationsService;


    @GetMapping()
    public Result<PageDTO<Reservations>> pageDTOResult(PageQuery pageQuery) {

      log.info("分页获取预约信息");
      PageDTO<Reservations> pageDTO = reservationsService.pageResult(pageQuery);
      return Result.success(pageDTO,"获取成功！");
    }


    @DeleteMapping("{id}")
    public Result deleteReserve(@PathVariable Long id) {
        log.info("删除预约记录id:" + id);

        reservationsService.removeById(id);
        return Result.success("删除成功！");
    }

    @PutMapping()
    //更新预约信息
    public Result updateReserve(@RequestBody ReservationsDTO reservationsDTO) {

        log.info("修改预约信息");
        reservationsService.updateReservations(reservationsDTO);
        return Result.success("修改成功!");
    }

}
