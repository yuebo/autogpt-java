package com.eappcat.autogpt.models.memory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputedMemory {
    private Memory memory;
    private double similarRate;
}
