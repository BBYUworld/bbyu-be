from fastapi import FastAPI, Depends, HTTPException
from sqlalchemy.orm import Session
from sqlalchemy import func, Column, BigInteger, Enum, Boolean, DateTime, Integer
from pydantic import BaseModel
from config import SessionLocal, Base, engine

app = FastAPI()

# DB 세션을 가져오는 의존성
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

# 모델 (ORM)
class Asset(Base):
    __tablename__ = 'asset'

    asset_id = Column(BigInteger, primary_key=True, index=True)
    user_id = Column(BigInteger)
    couple_id = Column(BigInteger)
    type = Column(Enum('DEPOSIT', 'STOCK', 'REAL_ESTATE', name="asset_type_enum"))
    amount = Column(BigInteger)
    created_at = Column(DateTime, default=func.now())
    updated_at = Column(DateTime, default=func.now(), onupdate=func.now())
    is_ended = Column(Boolean)

class YearlyAsset(Base):
    __tablename__ = 'yearly_asset'

    id = Column(BigInteger, primary_key=True, index=True)
    couple_id = Column(BigInteger, index=True)
    year = Column(Integer, index=True)
    cash_assets = Column(BigInteger)
    investment_assets = Column(BigInteger)
    real_estate_assets = Column(BigInteger)
    total_assets = Column(BigInteger)

# 모든 테이블 생성
Base.metadata.create_all(bind=engine)

# 자산 카테고리 응답 모델
class AssetCategoryResponse(BaseModel):
    label: str
    amount: int
    percentage: float

# API 엔드포인트 - 현재 자산 조회
@app.get("/api/coupleAssets/{coupleId}", response_model=list[AssetCategoryResponse])
def get_couple_assets(coupleId: int, db: Session = Depends(get_db)):
    # 부부의 총 자산 계산
    total_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId).scalar() or 0

    if total_assets == 0:
        raise HTTPException(status_code=404, detail="해당 부부의 자산이 없습니다.")

    # 자산 카테고리별 계산
    cash_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'DEPOSIT').scalar() or 0
    investment_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'STOCK').scalar() or 0
    real_estate_assets = db.query(func.sum(Asset.amount)).filter(Asset.couple_id == coupleId, Asset.type == 'REAL_ESTATE').scalar() or 0

    # 응답 데이터 준비
    data = [
        AssetCategoryResponse(label="현금(예적금)", amount=cash_assets, percentage=round((cash_assets / total_assets) * 100, 2)),
        AssetCategoryResponse(label="투자(주식, 채권, 펀드)", amount=investment_assets, percentage=round((investment_assets / total_assets) * 100, 2)),
        AssetCategoryResponse(label="부동산, 토지", amount=real_estate_assets, percentage=round((real_estate_assets / total_assets) * 100, 2)),
    ]

    return data

@app.post("/api/saveYearlyAsset")
def save_yearly_assets_for_all(db: Session = Depends(get_db)):
    # 모든 커플의 couple_id를 가져옴
    couples = db.query(Asset.couple_id).distinct().all()

    for couple in couples:
        couple_id = couple[0]

        # couple_id가 None이 아닌 경우에만 처리
        if couple_id is None:
            continue

        # 자산 집계
        cash_assets = db.query(func.sum(Asset.amount)).filter(
            Asset.couple_id == couple_id,
            Asset.type == 'DEPOSIT'
        ).scalar() or 0

        investment_assets = db.query(func.sum(Asset.amount)).filter(
            Asset.couple_id == couple_id,
            Asset.type == 'STOCK'
        ).scalar() or 0

        real_estate_assets = db.query(func.sum(Asset.amount)).filter(
            Asset.couple_id == couple_id,
            Asset.type == 'REAL_ESTATE'
        ).scalar() or 0

        total_assets = cash_assets + investment_assets + real_estate_assets

        # 연도 추출 (예: 자산이 생성된 연도의 가장 최근 값을 사용)
        year = db.query(func.year(func.max(Asset.created_at))).filter(Asset.couple_id == couple_id).scalar()

        # 연도가 None인 경우는 건너뜀
        if year is None:
            continue

        # 기존 연도별 자산 데이터 존재 여부 확인
        yearly_asset = db.query(YearlyAsset).filter(
            YearlyAsset.couple_id == couple_id,
            YearlyAsset.year == year
        ).first()

        if yearly_asset:
            raise HTTPException(status_code=400, detail=f"Couple ID {couple_id}의 연도 {year}에 대한 자산 데이터가 이미 존재합니다.")

        # 새로운 연도별 자산 데이터 생성
        new_yearly_asset = YearlyAsset(
            couple_id=couple_id,
            year=year,
            cash_assets=cash_assets,
            investment_assets=investment_assets,
            real_estate_assets=real_estate_assets,
            total_assets=total_assets
        )

        db.add(new_yearly_asset)

    db.commit()

    return {"message": "모든 커플의 연도별 자산 내역이 성공적으로 저장되었습니다."}


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
