#include <iostream>
#include <cmath>
#include "RGBPixel.hpp"

using namespace std;



// Constructor based on other pixel
RGBPixel::RGBPixel(const RGBPixel& p) {

    rgb = 0;
    rgb = rgb | p.getRed();
    rgb = rgb << 8;
    rgb = rgb | p.getGreen();
    rgb = rgb << 8;
    rgb = rgb | p.getBlue(); 
}

// Constructor based on rgb values
RGBPixel::RGBPixel(unsigned char r, unsigned char g, unsigned char b) {
    rgb = 0;
    rgb = rgb | (short) r;
    rgb = rgb << 8;
    rgb = rgb | (short) g;
    rgb = rgb << 8;
    rgb = rgb | (short) b;    
}


unsigned char RGBPixel::getRed() const {
    
    int mask = (int)pow(2,8) - 1;
    mask = mask << 16;
    int value = rgb & mask;
    value = value >> 16;

    return (unsigned char) (value);
}

unsigned char RGBPixel::getGreen() const {
    
    int mask = (int)pow(2,8) - 1;
    mask = mask << 8;
    int value = rgb & mask;
    value = value >> 8;

    return (unsigned char) (value);
}


unsigned char RGBPixel::getBlue() const {
    
    int mask = (int)pow(2,8) - 1;
    int value = rgb & mask;

    return (unsigned char) (value);
}

void RGBPixel::setRed(int r) {
    unsigned char green = getGreen();
    unsigned char blue = getBlue();
    rgb = (r << 16) | (green << 8) | blue;
}

void RGBPixel::setGreen(int g) {
    unsigned char red = getRed();
    unsigned char blue = getBlue();
    rgb = (red << 16) | (g << 8) | blue;
}

void RGBPixel::setBlue(int b) {
    unsigned char red = getRed();
    unsigned char green = getGreen();
    rgb = (red << 16) | (green << 8) | b;
}
