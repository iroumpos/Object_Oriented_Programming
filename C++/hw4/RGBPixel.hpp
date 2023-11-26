#ifndef RGBPIXEL_HPP
#define RGBPIXEL_HPP

#include "Pixel.hpp"

class RGBPixel : public Pixel{
    private:
        int rgb;
    public:
        RGBPixel() = default;
        RGBPixel(const RGBPixel& p);
        RGBPixel(unsigned char r, unsigned char g, unsigned char b);
        unsigned char getRed() const;
        unsigned char getGreen() const;
        unsigned char getBlue() const;
        void setRed(int r);
        void setGreen(int g);
        void setBlue(int b);
};


#endif