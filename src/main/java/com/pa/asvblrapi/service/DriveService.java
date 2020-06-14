package com.pa.asvblrapi.service;

import com.pa.asvblrapi.dto.DriveDto;
import com.pa.asvblrapi.dto.UserDtoFirebase;
import com.pa.asvblrapi.entity.Drive;
import com.pa.asvblrapi.entity.Match;
import com.pa.asvblrapi.entity.User;
import com.pa.asvblrapi.exception.*;
import com.pa.asvblrapi.mapper.DriveMapper;
import com.pa.asvblrapi.repository.DriveRepository;
import com.pa.asvblrapi.repository.MatchRepository;
import com.pa.asvblrapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriveService {
    @Autowired
    private DriveRepository driveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;

    public List<DriveDto> getAllDrive() {
        return DriveMapper.instance.toDto(this.driveRepository.findAll());
    }

    public DriveDto getDrive(Long id) throws DriveNotFoundException {
        return DriveMapper.instance.toDto(this.driveRepository.findById(id)
                .orElseThrow(() -> new DriveNotFoundException(id)));
    }

    public List<DriveDto> getAllByIdMatch(Long idMatch) throws MatchNotFoundException {
        Optional<Match> match = this.matchRepository.findById(idMatch);
        if (!match.isPresent()) {
            throw new MatchNotFoundException(idMatch);
        }
        return DriveMapper.instance.toDto(this.driveRepository.findAllByIdMatch(idMatch));
    }

    public List<DriveDto> getAllByIdDriver(Long idDriver) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(idDriver);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idDriver);
        }
        return DriveMapper.instance.toDto(this.driveRepository.findAllByIdDriver(idDriver));
    }

    public List<DriveDto> getAllByIdPassenger(Long idPassenger) throws UserNotFoundException {
        Optional<User> user = this.userRepository.findById(idPassenger);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idPassenger);
        }
        return DriveMapper.instance.toDto(this.driveRepository.findAllByIdPassenger(idPassenger));
    }

    public DriveDto createDrive(DriveDto driveDto) throws UserNotFoundException, MatchNotFoundException {
        Optional<User> driver = this.userRepository.findById(driveDto.getIdDriver());
        if (!driver.isPresent()) {
            throw new UserNotFoundException(driveDto.getIdDriver());
        }
        Optional<Match> match = this.matchRepository.findById(driveDto.getIdMatch());
        if (!match.isPresent()) {
            throw new MatchNotFoundException(driveDto.getIdMatch());
        }

        Drive drive = new Drive(driveDto.getAddress(), driveDto.isGo(), driveDto.getDate(), driveDto.getNbTotalPlaces(),
                driver.get(), match.get());

        return DriveMapper.instance.toDto(this.driveRepository.save(drive));
    }

    public DriveDto updateDrive(Long id, DriveDto driveDto)
            throws DriveNotFoundException, UserNotFoundException, MatchNotFoundException {
        Optional<Drive> drive = this.driveRepository.findById(id);
        if (!drive.isPresent()) {
            throw new DriveNotFoundException(id);
        }
        Optional<User> driver = this.userRepository.findById(driveDto.getIdDriver());
        if (!driver.isPresent()) {
            throw new UserNotFoundException(driveDto.getIdDriver());
        }
        Optional<Match> match = this.matchRepository.findById(driveDto.getIdMatch());
        if (!match.isPresent()) {
            throw new MatchNotFoundException(driveDto.getIdMatch());
        }

        drive.get().setAddress(driveDto.getAddress());
        drive.get().setGo(driveDto.isGo());
        drive.get().setDate(driveDto.getDate());
        drive.get().setNbTotalPlaces(driveDto.getNbTotalPlaces());
        drive.get().setDriver(driver.get());
        drive.get().setMatch(match.get());

        return DriveMapper.instance.toDto(this.driveRepository.save(drive.get()));
    }

    public List<UserDtoFirebase> getPassengers(Long id) throws DriveNotFoundException {
        Optional<Drive> drive = this.driveRepository.findById(id);
        if (!drive.isPresent()) {
            throw new DriveNotFoundException(id);
        }
        List<UserDtoFirebase> passengers = new ArrayList<>();
        for (User user :
                drive.get().getPassengers()) {
            passengers.add(
                    new UserDtoFirebase(
                            user.getId(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail()));
        }
        return passengers;
    }

    public DriveDto addPassenger(Long idDrive, Long idUser) throws DriveNotFoundException, UserNotFoundException {
        Optional<Drive> drive = this.driveRepository.findById(idDrive);
        if (!drive.isPresent()) {
            throw new DriveNotFoundException(idDrive);
        }
        Optional<User> user = this.userRepository.findById(idUser);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idUser);
        }
        if (drive.get().getNbFreePlaces() == 0) {
            throw new DriveIsFullException(idDrive);
        }
        if (drive.get().getPassengers().contains(user.get())) {
            throw new UserAlreadyInDriveException(idDrive, idUser);
        }
        drive.get().getPassengers().add(user.get());
        drive.get().setNbFreePlaces(drive.get().getNbFreePlaces() - 1);
        return DriveMapper.instance.toDto(this.driveRepository.save(drive.get()));
    }

    public DriveDto deletePassenger(Long idDrive, Long idUser)
            throws DriveNotFoundException, UserNotFoundException, UserNotFoundInDriveException {
        Optional<Drive> drive = this.driveRepository.findById(idDrive);
        if (!drive.isPresent()) {
            throw new DriveNotFoundException(idDrive);
        }
        Optional<User> user = this.userRepository.findById(idUser);
        if (!user.isPresent()) {
            throw new UserNotFoundException(idUser);
        }
        if (!drive.get().getPassengers().contains(user.get())) {
            throw new UserNotFoundInDriveException(idDrive, idUser);
        }
        drive.get().getPassengers().remove(user.get());
        drive.get().setNbFreePlaces(drive.get().getNbFreePlaces() + 1);
        return DriveMapper.instance.toDto(this.driveRepository.save(drive.get()));
    }

    public void deleteDrive(Long id) throws DriveNotFoundException {
        Optional<Drive> drive = this.driveRepository.findById(id);
        if (!drive.isPresent()) {
            throw new DriveNotFoundException(id);
        }
        this.driveRepository.delete(drive.get());
    }
}


































