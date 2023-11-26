#include <iostream>
#include "Token.hpp"


Token::Token(const string& name, Image* ptr) : name(name), ptr(ptr) {}


string Token::getName() const {
    return name;
}

Image* Token::getPtr() const {
    return ptr;
}

void Token::setName(const string& name) {
    this->name = name;
}

void Token::setPtr(Image* ptr) {
    this->ptr = ptr;
}

