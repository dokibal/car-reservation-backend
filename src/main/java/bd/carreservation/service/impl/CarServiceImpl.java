package bd.carreservation.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import bd.carreservation.exception.ResourceNotFoundException;
import bd.carreservation.model.Car;
import bd.carreservation.repository.CarRepository;
import bd.carreservation.repository.ReservationRepository;
import bd.carreservation.service.CarService;

@Service
public class CarServiceImpl implements CarService {
	private CarRepository carRepository;
	private ReservationRepository reservationRepository;

	public CarServiceImpl(CarRepository carRepository, ReservationRepository reservationRepository) {
		super();
		this.carRepository = carRepository;
		this.reservationRepository = reservationRepository;
	}

	@Override
	public List<Car> getCars() {
		return carRepository.findAll();
	}

	@Override
	public Car addCar(Car car) {
		return carRepository.save(car);
	}

	@Override
	public Car updateCar(Long id, Car car) {
		Car carToUpdate = carRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Car does not exist with id:" + id));
		carToUpdate.setBrand(car.getBrand());
		carToUpdate.setModel(car.getModel());
		carToUpdate.setRegistrationNumber(car.getRegistrationNumber());
		carToUpdate.setCapacity(car.getCapacity());

		carRepository.save(carToUpdate);

		return carToUpdate;
	}

	@Override
	public void removeCar(Long id) {

		Car car = carRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Car does not exist with id:" + id));

		// Delete all reservation records of the car
		reservationRepository.findReservationByCar(id).forEach(res -> {
			reservationRepository.deleteById(res.getId());
		});

		// Finally delete the car record
		carRepository.deleteById(id);
	}

}
