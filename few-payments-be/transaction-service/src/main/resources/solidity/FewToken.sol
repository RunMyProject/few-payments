/*
 * FewToken.sol
 * Author: Edoardo Sabatini
 * Date: 2026-05-14
 * Company: few-payments
 * Description: 
 */
// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

/**
 * @title FewToken
 * @dev Simple ERC20-like implementation without external dependencies for testing.
 */
contract FewToken {
    string public name = "FEW Payment Token";
    string public symbol = "FEW";
    uint8 public decimals = 18;
    uint256 public totalSupply = 1000000 * 10**18;

    mapping(address => uint256) public balanceOf;

    constructor() {
        balanceOf[msg.sender] = totalSupply;
    }

    function transfer(address to, uint256 amount) public returns (bool) {
        require(balanceOf[msg.sender] >= amount, "Insufficient balance");
        balanceOf[msg.sender] -= amount;
        balanceOf[to] += amount;
        return true;
    }
}
