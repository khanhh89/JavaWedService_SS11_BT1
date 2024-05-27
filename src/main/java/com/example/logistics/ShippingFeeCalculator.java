package com.example.logistics;

public class ShippingFeeCalculator {

    public double calculateFee(double weightKg, double distanceKm) {
        if (weightKg <= 0 || distanceKm <= 0) {
            throw new IllegalArgumentException("Weight and distance must be positive");
        }

        double weightFee = 0;
        if (weightKg <= 1) {
            weightFee = 50000;
        } else {
            // Sửa lỗi: Sử dụng Math.ceil thay vì Math.floor để làm tròn lên cho các số lẻ (phân số của kg)
            weightFee = 50000 + (Math.ceil(weightKg - 1) * 10000);
        }

        double distanceFee = 0;
        if (distanceKm < 10) {
            distanceFee = 0;
        } else if (distanceKm < 50) {
            // Sửa lỗi: Chỉ tính phí cho khoảng cách vượt quá 10km đầu tiên
            distanceFee = (distanceKm - 10) * 5000;
        } else {
            // Sửa lỗi: Tính phí lũy tiến: 40km ở mức 5000/km và phần còn lại ở mức 4000/km
            distanceFee = (40 * 5000) + ((distanceKm - 50) * 4000);
        }

        return weightFee + distanceFee;
    }
}