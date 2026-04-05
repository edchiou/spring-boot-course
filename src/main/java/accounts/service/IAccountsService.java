package accounts.service;

import accounts.dto.CustomerDto;

public interface IAccountsService {
    void createAccount(CustomerDto customerDto);

    CustomerDto getAccount(String mobileNumber);
}
