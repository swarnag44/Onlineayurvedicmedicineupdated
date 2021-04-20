package com.cg.oam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.oam.entity.UserEntity;
import com.cg.oam.exception.OrderNotFoundException;
import com.cg.oam.model.UserModel;
import com.cg.oam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTmplTest {
	
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	@Test
	@DisplayName("find all the users")
	void testFindAll() {
		List<UserEntity> testdata = Arrays.asList(new UserEntity[] {
				 new UserEntity(1L,"Swarna&123","customer"),
				 new UserEntity(2L,"Swarna&123","customer"),
		});
		Mockito.when(userRepository.findAll()).thenReturn(testdata);
		
		List<UserModel> expected = Arrays.asList(new UserModel[] {
				new UserModel(1L,"Swarna&123","customer"),
				new UserModel(2L,"Swarna&123","customer")
		
		});
		List<UserModel>actual = userServiceImpl.findAll();
		assertEquals(expected,actual);
		
	}
	
	@Test
	@DisplayName("get user by id")
	void testFindById() throws OrderNotFoundException {
		
		UserEntity testdata = new UserEntity(1L,"Swarna&123","customer");
		UserModel expected = new UserModel(1L,"Swarna&123","customer");
		
		
		Mockito.when(userRepository.findById(testdata.getCustomerId())).thenReturn(Optional.of(testdata));
		
		UserModel actual = userServiceImpl.findById(testdata.getCustomerId());
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("delete user by id")
	void testDeleteById() {
		Long customerId = 1L;
		userServiceImpl.deleteById(customerId);
		verify(userRepository,times(1)).deleteById(customerId);

	}
}
