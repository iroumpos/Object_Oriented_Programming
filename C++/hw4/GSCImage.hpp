#ifndef GSCIMAGE_HPP
#define GSCIMAGE_HPP

#include "Image.hpp"
#include "GSCPixel.hpp"
#include "RGBImage.hpp"

class GSCImage : public Image {
    private:
        GSCPixel** bw_pixel;   // black and white pixel

    public:
        GSCImage();
        GSCImage(const GSCImage& img);
        GSCImage(const RGBImage& grayscaled);
        GSCImage(std::istream& stream);
        ~GSCImage() override {
            for (int i = 0; i < height; i++) {
                delete[] bw_pixel[i];
            }
             delete[] bw_pixel;
        }

        GSCImage& operator = (const GSCImage& img);

        virtual Image& operator += (int ) override;
        

        virtual Image& operator *= (double factor) override;
        virtual Image& operator ! () override;
        virtual Image& operator ~ () override;
        virtual Image& operator * () override;

        virtual Pixel& getPixel(int row, int col) const override;

        friend std::ostream& operator << (std::ostream& out, Image& image);
};


#endif