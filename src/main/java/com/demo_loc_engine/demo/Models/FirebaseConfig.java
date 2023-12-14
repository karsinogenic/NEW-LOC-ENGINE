package com.demo_loc_engine.demo.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "firebase_config")
public class FirebaseConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "konten_value")
    private String konten_value;

    @Column(name = "type_trx_firebase")
    private String type_trx_firebase;

    @Column(name = "type_trx_table")
    private String type_trx_table;

    @Column(name = "notif")
    private String notif;

    @Column(name = "key_no")
    private String key_no;

    @Column(name = "channel_id")
    private String channel_id;
    @Column(name = "link_promo")
    private String link_promo;
    @Column(name = "url_image")
    private String url_image;
    @Column(name = "url_page")
    private String url_page;
    @Column(name = "deeplink")
    private String deeplink;

    @Column(name = "key_no_notifpromo")
    private String key_no_notifpromo;

    @Column(name = "notif_type")
    private String notif_type;

}
