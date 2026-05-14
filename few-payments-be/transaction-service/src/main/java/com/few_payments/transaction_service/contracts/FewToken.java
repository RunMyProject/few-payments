package com.few_payments.transaction_service.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.10.0.
 */
@SuppressWarnings("rawtypes")
public class FewToken extends Contract {
    public static final String BINARY = "60c060405260116080908152702322ab902830bcb6b2b73a102a37b5b2b760791b60a0525f9061002f908261012e565b5060408051808201909152600381526246455760e81b6020820152600190610057908261012e565b506002805460ff1916601217905569d3c21bcecceda100000060035534801561007e575f5ffd5b50600354335f908152600460205260409020556101e8565b634e487b7160e01b5f52604160045260245ffd5b600181811c908216806100be57607f821691505b6020821081036100dc57634e487b7160e01b5f52602260045260245ffd5b50919050565b601f82111561012957805f5260205f20601f840160051c810160208510156101075750805b601f840160051c820191505b81811015610126575f8155600101610113565b50505b505050565b81516001600160401b0381111561014757610147610096565b61015b8161015584546100aa565b846100e2565b6020601f82116001811461018d575f83156101765750848201515b5f19600385901b1c1916600184901b178455610126565b5f84815260208120601f198516915b828110156101bc578785015182556020948501946001909201910161019c565b50848210156101d957868401515f19600387901b60f8161c191681555b50505050600190811b01905550565b61038d806101f55f395ff3fe608060405234801561000f575f5ffd5b5060043610610060575f3560e01c806306fdde031461006457806318160ddd14610082578063313ce5671461009957806370a08231146100b857806395d89b41146100d7578063a9059cbb146100df575b5f5ffd5b61006c610102565b604051610079919061024d565b60405180910390f35b61008b60035481565b604051908152602001610079565b6002546100a69060ff1681565b60405160ff9091168152602001610079565b61008b6100c636600461029d565b60046020525f908152604090205481565b61006c61018d565b6100f26100ed3660046102bd565b61019a565b6040519015158152602001610079565b5f805461010e906102e5565b80601f016020809104026020016040519081016040528092919081815260200182805461013a906102e5565b80156101855780601f1061015c57610100808354040283529160200191610185565b820191905f5260205f20905b81548152906001019060200180831161016857829003601f168201915b505050505081565b6001805461010e906102e5565b335f908152600460205260408120548211156101f35760405162461bcd60e51b8152602060048201526014602482015273496e73756666696369656e742062616c616e636560601b604482015260640160405180910390fd5b335f9081526004602052604081208054849290610211908490610331565b90915550506001600160a01b0383165f908152600460205260408120805484929061023d908490610344565b9091555060019150505b92915050565b602081525f82518060208401528060208501604085015e5f604082850101526040601f19601f83011684010191505092915050565b80356001600160a01b0381168114610298575f5ffd5b919050565b5f602082840312156102ad575f5ffd5b6102b682610282565b9392505050565b5f5f604083850312156102ce575f5ffd5b6102d783610282565b946020939093013593505050565b600181811c908216806102f957607f821691505b60208210810361031757634e487b7160e01b5f52602260045260245ffd5b50919050565b634e487b7160e01b5f52601160045260245ffd5b818103818111156102475761024761031d565b808201808211156102475761024761031d56fea26469706673582212202f64a606efa4b723de338518def5d75e00739a9a872920774664fe585f1bad6a64736f6c634300081e0033";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_TRANSFER = "transfer";

    @Deprecated
    protected FewToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected FewToken(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected FewToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected FewToken(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> balanceOf(String param0) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transfer(String to, BigInteger amount) {
        final Function function = new Function(
                FUNC_TRANSFER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, to), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static FewToken load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new FewToken(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static FewToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new FewToken(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static FewToken load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new FewToken(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static FewToken load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new FewToken(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<FewToken> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(FewToken.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<FewToken> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(FewToken.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<FewToken> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FewToken.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<FewToken> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FewToken.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
