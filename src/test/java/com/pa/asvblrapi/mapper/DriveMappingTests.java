package com.pa.asvblrapi.mapper;

import com.pa.asvblrapi.dto.DriveDto;
import com.pa.asvblrapi.entity.Drive;
import com.pa.asvblrapi.entity.Match;
import com.pa.asvblrapi.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DriveMappingTests {

    @Test
    public void should_map_drive_to_dto() {
        User user = new User();
        user.setId((long) 2);
        Match match = new Match();
        match.setId((long) 3);
        Drive drive = new Drive((long) 1, "address",true, new Date(), 3, 3, user, match, new ArrayList<>());

        DriveDto driveDto = DriveMapper.instance.toDto(drive);

        assertThat(driveDto).isNotNull();
        assertThat(driveDto.getId()).isEqualTo(drive.getId());
        assertThat(driveDto.getAddress()).isEqualTo(drive.getAddress());
        assertThat(driveDto.isGo()).isEqualTo(drive.isGo());
        assertThat(driveDto.getDate()).isEqualTo(drive.getDate());
        assertThat(driveDto.getNbTotalPlaces()).isEqualTo(drive.getNbTotalPlaces());
        assertThat(driveDto.getNbFreePlaces()).isEqualTo(drive.getNbFreePlaces());
        assertThat(driveDto.getIdDriver()).isEqualTo(user.getId());
        assertThat(driveDto.getIdMatch()).isEqualTo(match.getId());
    }
}
