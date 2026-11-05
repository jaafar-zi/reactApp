package com.ecommerce.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class StorageResult {
    String storedFileName;
    String storagePath;
    String checksum;
}
