package com.example.demo.entity;

import java.io.Serializable;

//@Table(name = "tb_Friend")
//@Entity(name = "tb_Friend")
public class Friend implements Serializable {
//	private static final long serialVersionUID = -6417332392376109675L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private UUID id;
//
//	@Column(nullable = false)
//	private String img;
//	@Column(nullable = false)
//	private String name;
//
//	// para comparar se já existe em nossa lista de amigos
//	@Column(nullable = false)
//	private UUID idFriend;
//
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	public Friend() {
//	}
//
//	public Friend(UUID id, String img, String name, UUID idFriend) {
//		super();
//		this.id = id;
//		this.img = img;
//		this.name = name;
//		this.idFriend = idFriend;
//	}
//
//	public Friend(UUID id, String img, String name, UUID idFriend, User user) {
//		super();
//		this.id = id;
//		this.img = img;
//		this.name = name;
//		this.idFriend = idFriend;
//		this.user = user;
//	}
//
//	public UUID getId() {
//		return id;
//	}
//
//	public void setId(UUID id) {
//		this.id = id;
//	}
//
//	public String getImg() {
//		return img;
//	}
//
//	public void setImg(String img) {
//		this.img = img;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public UUID getIdFriend() {
//		return idFriend;
//	}
//
//	public void setIdFriend(UUID idFriend) {
//		this.idFriend = idFriend;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	@Override
//	public String toString() {
//		return "Friend [id=" + id + ", img=" + img + ", name=" + name + ", idFriend=" + idFriend + "]";
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(id);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Friend other = (Friend) obj;
//		return Objects.equals(id, other.id);
//	}

}
