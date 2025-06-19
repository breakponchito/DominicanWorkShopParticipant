package com.book.store.model;

public record BookDTO(Long id,String title, Author author, String description, String imageName, double price) {
    
}

