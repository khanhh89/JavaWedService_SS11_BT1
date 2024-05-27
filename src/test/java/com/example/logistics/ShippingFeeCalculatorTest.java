package com.example.logistics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.*;

class ShippingFeeCalculatorTest {

    private ShippingFeeCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new ShippingFeeCalculator();
    }

    @Test
    void shouldReturnBaseFee_whenWeightIsOneKgOrLessAndDistanceIsLessThan10Km() {
        // Cân nặng nhỏ hơn hoặc bằng 1kg, khoảng cách nhỏ hơn 10km.
        double fee = calculator.calculateFee(0.5, 5);
        assertThat(fee).isEqualTo(50000);

        fee = calculator.calculateFee(1.0, 9.9);
        assertThat(fee).isEqualTo(50000);
    }

    @Test
    void shouldCalculateCorrectly_whenWeightIsMoreThanOneKgAndDistanceIsBetween10And50Km() {
        // Cân nặng lớn hơn 1kg (số nguyên), khoảng cách trong khoảng 10km đến 50km.
        // Weight: 3kg -> Base 50000 + (3-1)*10000 = 70000
        // Distance: 20km -> (20-10)*5000 = 50000
        // Total: 120000
        double fee = calculator.calculateFee(3.0, 20.0);
        assertThat(fee).isEqualTo(120000);
    }

    @Test
    void shouldCalculateCorrectly_whenWeightIsFractionalAndDistanceIsMoreThan50Km() {
        // Cân nặng là số lẻ (ví dụ: 1.5kg, 2.3kg), khoảng cách lớn hơn 50km.
        // Weight: 1.5kg -> Math.ceil(0.5) = 1. Base 50000 + 1*10000 = 60000
        // Distance: 60km -> (40 * 5000) + (10 * 4000) = 200000 + 40000 = 240000
        // Total: 300000
        double fee1 = calculator.calculateFee(1.5, 60.0);
        assertThat(fee1).isEqualTo(300000);
        
        // Weight: 2.3kg -> Math.ceil(1.3) = 2. Base 50000 + 2*10000 = 70000
        // Distance: 70km -> (40 * 5000) + (20 * 4000) = 200000 + 80000 = 280000
        // Total: 350000
        double fee2 = calculator.calculateFee(2.3, 70.0);
        assertThat(fee2).isEqualTo(350000);
    }

    @Test
    void shouldCalculateCorrectly_atBoundaryDistances() {
        // Khoảng cách đúng 10km
        // Distance = 10km -> (10-10)*5000 = 0
        // Weight: 1kg -> 50000
        double fee10 = calculator.calculateFee(1.0, 10.0);
        assertThat(fee10).isEqualTo(50000);

        // Khoảng cách đúng 50km
        // Distance = 50km -> (40*5000) + (0*4000) = 200000
        // Weight: 1kg -> 50000
        double fee50 = calculator.calculateFee(1.0, 50.0);
        assertThat(fee50).isEqualTo(250000);
    }

    @ParameterizedTest
    @CsvSource({
            "0, 10",
            "-1, 10",
            "1, 0",
            "1, -5",
            "0, 0"
    })
    void shouldThrowException_whenInputsAreInvalid(double weight, double distance) {
        // Kiểm tra trường hợp đầu vào không hợp lệ (cân nặng hoặc khoảng cách <= 0, ném IllegalArgumentException).
        assertThatThrownBy(() -> calculator.calculateFee(weight, distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Weight and distance must be positive");
    }
}