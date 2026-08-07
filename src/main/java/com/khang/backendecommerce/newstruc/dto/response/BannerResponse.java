package com.khang.backendecommerce.newstruc.dto.response;

public record BannerResponse(
  String id ,
  String title,
  String description,
  String imageUrl,
  String buttonText,
  String targetUrl
) {
}
