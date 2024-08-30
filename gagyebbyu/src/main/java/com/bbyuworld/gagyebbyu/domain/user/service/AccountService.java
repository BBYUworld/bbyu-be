package com.bbyuworld.gagyebbyu.domain.user.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.bbyuworld.gagyebbyu.domain.asset.dto.CreateDepositDto;
import com.bbyuworld.gagyebbyu.domain.asset.dto.CreateSavingDto;
import com.bbyuworld.gagyebbyu.domain.asset.entity.AssetAccount;
import com.bbyuworld.gagyebbyu.domain.asset.enums.AccountType;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetAccountRepository;
import com.bbyuworld.gagyebbyu.domain.asset.repository.AssetRepository;
import com.bbyuworld.gagyebbyu.domain.couple.entity.Couple;
import com.bbyuworld.gagyebbyu.domain.couple.repository.CoupleRepository;
import com.bbyuworld.gagyebbyu.domain.user.entity.User;
import com.bbyuworld.gagyebbyu.domain.user.repository.UserRepository;
import com.bbyuworld.gagyebbyu.global.api.asset.CreateDemandDepositAccountDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.AccountDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DemandBalanceDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DemandDepositDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.DepositDto;
import com.bbyuworld.gagyebbyu.global.api.demanddeposit.WithdrawalDto;
import com.bbyuworld.gagyebbyu.global.util.ApiPost;
import com.bbyuworld.gagyebbyu.global.util.HeaderProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
	@Value("${ssafy.api_key}")
	private String apiKey;
	private final ApiPost apiPost;
	private final AssetRepository assetRepository;
	private final CoupleRepository coupleRepository;
	private final UserRepository userRepository;
	private final AssetAccountRepository assetAccountRepository;

	public List<DemandDepositDto> findAllDemandDeposit() {
		return sendPostAboutFindAllProducts();
	}

	public List<AccountDto> findAllUserAccount(Long userId) {
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		List<AccountDto> list = sendPostAboutUserAccount(userKey);
		System.out.println("user Account = " + list);
		return list;
	}

	public List<AccountDto> findAllCoupleAccount(Long userId) {
		User user = userRepository.findUserById(userId);
		Couple couple = coupleRepository.getReferenceById(user.getCoupleId());
		User user1 = couple.getUser1();
		User user2 = couple.getUser2();
		String userKey = user.getApiKey();
		List<AccountDto> list = sendPostAboutUserAccount(user1.getApiKey());
		List<AccountDto> list2 = sendPostAboutUserAccount(user2.getApiKey());
		List<AccountDto> result = new ArrayList<>();
		result.addAll(list);
		result.addAll(list2);
		System.out.println("user Account = " + list);
		return result;
	}

	public DemandBalanceDto findDemandBalance(Long userId, String accountNo) {
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		return inquireDemandDepositAccountBalance(userKey, accountNo);
	}

	@Transactional
	public DepositDto updateDeposit(Long userId, String accountNo, String transactionBalance,
		String transactionSummary) {
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		return updateDemandDepositAccountDeposit(userKey, accountNo, transactionBalance, transactionSummary);
	}

	@Transactional
	public WithdrawalDto updateWithdrawal(Long userId, String accountNo, String transactionBalance,
		String transactionSummary) {
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		return updateDemandDepositAccountWithdrawal(userKey, accountNo, transactionBalance, transactionSummary);
	}

	@Transactional
	public AccountDto createUserAccount(Long userId, String uniqueNo, String bankName, Long dailyTransferLimit,
		Long oneTimeTransferLimit) {
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		CreateDemandDepositAccountDto dto = sendPostAboutCreateUserAccount(userKey, uniqueNo);
		System.out.println("Create Dto = " + dto);
		AccountDto updateDto = sendPostAboutUpdateTransferLimit(userKey, dto.getAccountNo(), dailyTransferLimit,
			oneTimeTransferLimit);
		if (updateDto == null)
			return null;
		return updateDto;
	}

	private List<AccountDto> sendPostAboutUserAccount(String userKey) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountList";
			String apiName = "inquireDemandDepositAccountList";
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode rootNode = objectMapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, objectMapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			String jsonBody = objectMapper.writeValueAsString(rootNode);
			httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = objectMapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				if (recNode != null && recNode.isArray()) {
					return objectMapper.convertValue(recNode, new TypeReference<List<AccountDto>>() {
					});
				} else {
					return new ArrayList<>();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<DemandDepositDto> sendPostAboutFindAllProducts() {
		System.out.println("Call Find All Products");
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositList";
			String apiName = "inquireDemandDepositList";
			HttpPost httpPost = new HttpPost(url);
			httpPost.setHeader("Content-Type", "application/json");
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode rootNode = objectMapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, objectMapper, apiKey);
			rootNode.set("Header", headerNode);
			String jsonBody = objectMapper.writeValueAsString(rootNode);
			httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = objectMapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = " + jsonResponse);
				if (recNode == null)
					return null;
				if (recNode.isArray()) {
					return objectMapper.convertValue(recNode, new TypeReference<List<DemandDepositDto>>() {
					});
				} else {
					return new ArrayList<>();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private CreateDemandDepositAccountDto sendPostAboutCreateUserAccount(String userKey, String uniqueNo) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/createDemandDepositAccount";
			String apiName = "createDemandDepositAccount";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("accountTypeUniqueNo", uniqueNo);
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));

            try(CloseableHttpResponse response = client.execute(httpPost)){
                String jsonResponse = EntityUtils.toString(response.getEntity());
                JsonNode responseRootNode = mapper.readTree(jsonResponse);
                JsonNode recNode = responseRootNode.get("REC");
                System.out.println("json Response = "+jsonResponse);
                if(recNode == null) return null;
                if (recNode != null) {
                    CreateDemandDepositAccountDto dto = mapper.treeToValue(recNode, CreateDemandDepositAccountDto.class);
                    System.out.println("Converted DTO: " + dto);
                    return dto;
                } else {
                    System.out.println("REC node is null in the response");
                    return null;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private AccountDto sendPostAboutUpdateTransferLimit(String userKey, String accountNo, Long dailyTransferLimit, Long oneTimeTransferLimit){
        try(CloseableHttpClient client = HttpClients.createDefault()){
            String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/updateTransferLimit";
            String apiName = "updateTransferLimit";
            ObjectMapper mapper = new ObjectMapper();
            HttpPost httpPost = new HttpPost(url);
            ObjectNode rootNode = mapper.createObjectNode();
            ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
            headerNode.put("userKey", userKey);
            rootNode.set("Header", headerNode);
            rootNode.put("accountNo", accountNo);
            rootNode.put("oneTimeTransferLimit", oneTimeTransferLimit);
            rootNode.put("dailyTransferLimit", dailyTransferLimit);
            httpPost.setHeader("Content-Type", "application/json");
            String jsonBody = mapper.writeValueAsString(rootNode);
            System.out.println("Request Body: " + jsonBody);
            httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = " + jsonResponse);
				if (recNode == null)
					return null;
				if (recNode != null) {
					AccountDto dto = mapper.treeToValue(recNode, AccountDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}


	private DemandBalanceDto inquireDemandDepositAccountBalance(String userKey, String accountNo) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/inquireDemandDepositAccountBalance";
			String apiName = "inquireDemandDepositAccountBalance";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("accountNo", accountNo);
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = " + jsonResponse);
				if (recNode == null)
					return null;
				if (recNode != null) {
					DemandBalanceDto dto = mapper.treeToValue(recNode,
						DemandBalanceDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private DepositDto updateDemandDepositAccountDeposit(String userKey, String accountNo,
		String transactionBalance, String transactionSummary) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/updateDemandDepositAccountDeposit";
			String apiName = "updateDemandDepositAccountDeposit";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("accountNo", accountNo);
			rootNode.put("transactionBalance", transactionBalance);
			rootNode.put("transactionSummary", transactionSummary);
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = " + jsonResponse);
				if (recNode == null)
					return null;
				if (recNode != null) {
					DepositDto dto = mapper.treeToValue(recNode,
						DepositDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	private WithdrawalDto updateDemandDepositAccountWithdrawal(String userKey, String accountNo,
		String transactionBalance, String transactionSummary) {
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/demandDeposit/updateDemandDepositAccountWithdrawal";
			String apiName = "updateDemandDepositAccountWithdrawal";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("accountNo", accountNo);
			rootNode.put("transactionBalance", transactionBalance);
			rootNode.put("transactionSummary", transactionSummary);
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));

			try (CloseableHttpResponse response = client.execute(httpPost)) {
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = " + jsonResponse);
				if (recNode == null)
					return null;
				if (recNode != null) {
					WithdrawalDto dto = mapper.treeToValue(recNode,
						WithdrawalDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}


	public String createSavingAccount(Long userId, String accountTypeUniqueNo, Long depositBalance, String withdrawalAccountNo){
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		AssetAccount assetAccount = assetAccountRepository.findAssetAccountByAccountNo(withdrawalAccountNo);
		if(assetAccount == null){
			return "계좌없음";
		}
		if(assetAccount.getAmount() - depositBalance < 0){
			return "잔액 부족";
		}
		CreateSavingDto dto = sendPostAboutCreateSavingAccount(userKey, accountTypeUniqueNo, depositBalance, withdrawalAccountNo);
		AssetAccount assetDepositAccount = new AssetAccount();
		assetAccount.setAmount(assetAccount.getAmount()-depositBalance);
		assetDepositAccount.setAccountType(AccountType.SAVINGS);
		assetDepositAccount.setAmount(depositBalance);
		assetDepositAccount.setAccountNumber(dto.getAccountNo());
		assetDepositAccount.setMaturityDate(LocalDate.now());
		assetDepositAccount.setTerm(Integer.valueOf(dto.getSubscriptionPeriod()));
		assetDepositAccount.setBankCode(dto.getBankCode());
		assetDepositAccount.setCreatedAt(LocalDateTime.now());
		assetDepositAccount.setUpdatedAt(LocalDateTime.now());
		assetDepositAccount.setBankName(dto.getBankName());
		BigDecimal interestRate = new BigDecimal(dto.getInterestRate());
		assetDepositAccount.setInterestRate(interestRate);
		user.getAssets().add(assetDepositAccount);
		assetDepositAccount.setUser(user);
		userRepository.save(user);
		return "ok";

	}
	private CreateSavingDto sendPostAboutCreateSavingAccount(String userKey, String accountTypeUniqueNo, Long depositBalance, String withdrawalAccountNo){
		try(CloseableHttpClient client = HttpClients.createDefault()){
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/savings/createAccount";
			String apiName = "createAccount";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("accountTypeUniqueNo", accountTypeUniqueNo);
			rootNode.put("depositBalance", String.valueOf(depositBalance));
			rootNode.put("withdrawalAccountNo", withdrawalAccountNo);
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));
			try(CloseableHttpResponse response = client.execute(httpPost)){
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = "+jsonResponse);
				if(recNode == null) return null;
				if (recNode != null) {
					CreateSavingDto dto = mapper.treeToValue(recNode, CreateSavingDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String createDepositAccount(Long userId, String accountNo, Long amount, String accountTypeUniqueNo){
		User user = userRepository.findUserById(userId);
		String userKey = user.getApiKey();
		AssetAccount assetAccount =assetAccountRepository.findAssetAccountByAccountNo(accountNo);
		if(assetAccount == null){
			return "계좌 없음";
		}
		if(assetAccount.getAmount() - amount < 0){
			return "잔액 부족";
		}
		CreateDepositDto dto = sendPostAboutCreateDepositAccount(userKey, accountNo, amount, accountTypeUniqueNo);
		AssetAccount assetDepositAccount = new AssetAccount();
		assetAccount.setAmount(assetAccount.getAmount()-amount);
		assetDepositAccount.setAccountType(AccountType.DEPOSIT);
		assetDepositAccount.setAmount(amount);
		assetDepositAccount.setAccountNumber(dto.getAccountNo());
		assetDepositAccount.setMaturityDate(LocalDate.now());
		assetDepositAccount.setTerm(Integer.valueOf(dto.getSubscriptionPeriod()));
		assetDepositAccount.setBankCode(dto.getBankCode());
		assetDepositAccount.setCreatedAt(LocalDateTime.now());
		assetDepositAccount.setUpdatedAt(LocalDateTime.now());
		assetDepositAccount.setBankName(dto.getBankName());
		BigDecimal interestRate = new BigDecimal(dto.getInterestRate());
		assetDepositAccount.setInterestRate(interestRate);
		user.getAssets().add(assetDepositAccount);
		assetDepositAccount.setUser(user);
		userRepository.save(user);
		return "ok";
	}

	private CreateDepositDto sendPostAboutCreateDepositAccount(String userKey, String accountNo, Long amount, String accountTypeUniqueNo){
		try(CloseableHttpClient client = HttpClients.createDefault()){
			String url = "https://finopenapi.ssafy.io/ssafy/api/v1/edu/deposit/createDepositAccount";
			String apiName = "createDepositAccount";
			ObjectMapper mapper = new ObjectMapper();
			HttpPost httpPost = new HttpPost(url);
			ObjectNode rootNode = mapper.createObjectNode();
			ObjectNode headerNode = HeaderProvider.createHeaderNode(apiName, mapper, apiKey);
			headerNode.put("userKey", userKey);
			rootNode.set("Header", headerNode);
			rootNode.put("withdrawalAccountNo", accountNo);
			rootNode.put("accountTypeUniqueNo", accountTypeUniqueNo);
			rootNode.put("depositBalance", String.valueOf(amount));
			httpPost.setHeader("Content-Type", "application/json");
			String jsonBody = mapper.writeValueAsString(rootNode);
			System.out.println("Request Body: " + jsonBody);
			httpPost.setEntity(new StringEntity(jsonBody));
			try(CloseableHttpResponse response = client.execute(httpPost)){
				String jsonResponse = EntityUtils.toString(response.getEntity());
				JsonNode responseRootNode = mapper.readTree(jsonResponse);
				JsonNode recNode = responseRootNode.get("REC");
				System.out.println("json Response = "+jsonResponse);
				if(recNode == null) return null;
				if (recNode != null) {
					CreateDepositDto dto = mapper.treeToValue(recNode, CreateDepositDto.class);
					System.out.println("Converted DTO: " + dto);
					return dto;
				} else {
					System.out.println("REC node is null in the response");
					return null;
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
